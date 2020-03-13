/**
 * Copyright 2020 Rahul Bagchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dreammaker.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * An android Spinner widget which support both single choice and multiple choice.
 * This Spinner shows a dialog of items. While choosing the items from the dialog,
 * the selections a stored temporarily and which can only be accessed
 * through the methods of {@link SpinnerSelectionData}
 * attached with the {@link SpinnerAdapter} with this Spinner.
 * After choosing the items when the dialog positive button is clicked then the selections are stored
 * permanently. The methods of SpinnerSelectionData can only be used when the SpinnerDialog is showing,
 * if the methods are used while dialog is dismissed then it will throw an {@link RuntimeException}
 *
 * @see dreammaker.android.widget.Spinner.SpinnerSelectionData
 * @see dreammaker.android.widget.SpinnerAdapter
 */
public class Spinner extends AppCompatEditText {

    private static final String TAG = "Spinner";

    public static final int NO_POSITION = -1;
    public static final long NO_ID = -1L;

    public static final int SINGLE_CHOICE = 1;
    public static final int MULTIPLE_CHOICE = 2;

    private int choiceMode = SINGLE_CHOICE;
    private List<Integer> selectedPositions;
    private List<Long> selectedIds;
    private SparseBooleanArray tmpSelectedPositions;
    private int selectedPosition = NO_POSITION;
    private int oldSelectedPosition = NO_POSITION;
    private long selectedId = NO_ID;
    private long oldSelectedId = NO_ID;
    private int tmpSelectedPosition = NO_POSITION;
    private int tmpOldSelectedPosition = NO_POSITION;

    private SpinnerSelectionData data;
    private SpinnerAdapter adapter;
    private DataSetObserver dataSetObserver;
    private boolean isDialogShowing = false;

    private String dialogTitle;
    private String dialogPositiveText;
    private String dialogNegativeText;
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    private ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (null != onSpinnerItemClickListener && onSpinnerItemClickListener.onClickSpinnerItem(Spinner.this, position)) return;
            data.toggleSelection(position);
        }
    };
    private ListView list;
    private Dialog spinnerDialog;
    private LayoutInflater inflater;

    private OnSpinnerItemClickListener onSpinnerItemClickListener;

    public Spinner(Context context) {
        super(context);
        init(context, null);
    }

    public Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Spinner);
        choiceMode = a.getInt(R.styleable.Spinner_choiceMode, SINGLE_CHOICE);
        if (a.hasValue(R.styleable.Spinner_dialogTitle)) {
            dialogTitle = a.getString(R.styleable.Spinner_dialogTitle);
        }
        else {
            dialogTitle = context.getString(R.string.spinner_dialog_title);
        }
        if (a.hasValue(R.styleable.Spinner_dialogPositiveText)) {
            dialogPositiveText = a.getString(R.styleable.Spinner_dialogPositiveText);
        }
        else {
            dialogPositiveText = context.getString(R.string.spinner_dialog_positive_text);
        }
        if (a.hasValue(R.styleable.Spinner_dialogNegativeText)) {
            dialogNegativeText = a.getString(R.styleable.Spinner_dialogNegativeText);
        }
        else {
            dialogNegativeText = context.getString(R.string.spinner_dialog_negative_text);
        }
        a.recycle();

        selectedPositions = new ArrayList<>();
        selectedIds = new ArrayList<>();
        tmpSelectedPositions = new SparseBooleanArray();
        data = new SpinnerSelectionData();
        dataSetObserver = new AdapterDataSetObserver();
    }

    @Override
    protected final MovementMethod getDefaultMovementMethod() {
        return null;
    }

    @Override
    protected final boolean getDefaultEditable() {
        return false;
    }

    /**
     * Spinner dialog is shown when the this widget is clicked
     * if it is enabled, otherwise no change happens
     *
     * @return same as the super method
     */
    @Override
    public boolean performClick() {
        super.performClick();
        if (!isEnabled()) return false;
        if (null != adapter) {
            if (SINGLE_CHOICE == choiceMode) {
                tmpOldSelectedPosition = oldSelectedPosition;
                tmpSelectedPosition = selectedPosition;
            } else {
                tmpSelectedPositions.clear();
                for (Integer selectedPosition : selectedPositions){
                    tmpSelectedPositions.put(selectedPosition, Boolean.TRUE);
                }
            }
            showSpinnerDialog();
        }
        return true;
    }

    /**
     * @return the current choice mode
     */
    public int getChoiceMode() {
        return choiceMode;
    }

    /**
     * Change the choice mode. If the previous choice and new choice mode is same then nothing happens,
     * otherwise all old selections are cleared and if the spinner is showing then it is dismissed
     *
     * @param choiceMode the new choice mode
     */
    public void setChoiceMode(int choiceMode) {
        if (SINGLE_CHOICE != choiceMode && MULTIPLE_CHOICE != choiceMode){
            throw new RuntimeException("unknown choice mode => "+choiceMode);
        }
        if (this.choiceMode == choiceMode) return;
        clearSelections();
        this.choiceMode = choiceMode;
    }

    public void setOnSpinnerItemClickListener(OnSpinnerItemClickListener onSpinnerItemClickListener) {
        this.onSpinnerItemClickListener = onSpinnerItemClickListener;
    }

    public OnSpinnerItemClickListener getOnSpinnerItemClickListener() {
        return onSpinnerItemClickListener;
    }

    /**
     * Returns the list adapter positions of the selected items when the choice mode is multiple.
     * It returns an empty list if the choice mode is other than MULTIPLE_CHOICE
     *
     * @return selected items positions
     */
    public List<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    /**
     * Set the list of adapter positions of the selected items when the choice mode MULTIPLE_CHOICE
     *
     * @param newPositions list of adapter positions of the selected items
     */
    public void setSelectedPositions(List<Integer> newPositions) {
        dismissSpinnerDialog();
        if (MULTIPLE_CHOICE == choiceMode) {
            selectedPositions.clear();
            selectedIds.clear();
            if (null == newPositions)return;
            for (Integer position : newPositions){
                selectedPositions.add(position);
                selectedIds.add(adapter.getItemId(position));
            }
            updateText();
        }
    }

    /**
     * Returns the list of ids of the selected items when the choice mode is MULTIPLE_CHOICE.
     * It returns an empty list if the choice mode is other than MULTIPLE_CHOICE.
     *
     * @return list of ids of the selected items.
     */
    public List<Long> getSelectedIds() {
        return selectedIds;
    }

    /**
     * Set the list of ids of the selected items when the choice mode is MULTIPLE_CHOICE.
     * Setting the selected items ids is useless when the adapter items has no fixed ids.
     * This method also populates the item positions for the given selected items ids by the
     * method {@link SpinnerAdapter#getPositionForId(long)}}.
     * The position and id are stored only for the items for which the above method returns value more than -1.
     *
     * @param newIds list of the ids of the selected items
     *
     * @see dreammaker.android.widget.SpinnerAdapter#getPositionForId(long)
     */
    public void setSelectedIds(List<Long> newIds) {
        dismissSpinnerDialog();
        if (MULTIPLE_CHOICE == choiceMode) {
            selectedPositions.clear();
            selectedIds.clear();
            if (null == newIds) return;
            for (Long id : newIds) {
                int position = adapter.getPositionForId(id);
                if (position >= 0) {
                    selectedPositions.add(position);
                    selectedIds.add(id);
                }
            }
            updateText();
        }
    }

    /**
     * @return the selected item position when the choice mode is SINGLE_CHOICE
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * @return the selected item id when the choice mode is SINGLE_CHOICE
     */
    public long getSelectedId() {
        return selectedId;
    }

    /**
     * Sets the selected item id. This method is use for when the adapter items has fixed unique item id.
     * It also calculate the item position in adapter by the
     * method {@link SpinnerAdapter#getPositionForId(long)}.
     * The item position and id is stored only when the above returns value more than -1
     *
     * @param selectedId the adapter item id of the selected item
     */
    public void setSelectedId(long selectedId) {
        dismissSpinnerDialog();
        if (SINGLE_CHOICE == choiceMode){
            int position = adapter.getPositionForId(selectedId);
            if (position > -1 && position != selectedPosition){
                oldSelectedPosition = selectedPosition;
                oldSelectedId = this.selectedId;
                selectedPosition = position;
                this.selectedId = selectedId;
            }
            updateText();
        }
    }

    /**
     * Sets an item selected or unselected at the position
     *
     * @param position the adapter position of the item to set selected
     * @param select the selection of the item
     */
    public void setSelected(int position, boolean select){
        dismissSpinnerDialog();
        if (isSelected(position) == select) return;
        if (SINGLE_CHOICE == choiceMode) {
            if (select) {
                oldSelectedPosition = selectedPosition;
                oldSelectedId = selectedId;
                selectedPosition = position;
                selectedId = adapter.getItemId(position);
            }
        } else {
            if (select) {
                selectedPositions.add(position);
                selectedIds.add(adapter.getItemId(position));
            } else {
                selectedPositions.remove(Integer.valueOf(position));
                selectedIds.remove(adapter.getItemId(position));
            }
        }
        updateText();
    }

    /**
     * Alters the selection of the item at the position
     *
     * @param position the adapter position of the item
     * @return returns the selection of the item
     */
    public boolean toggleSelection(int position){
        dismissSpinnerDialog();
        boolean newSelection = !isSelected(position);
        setSelected(position, newSelection);
        return newSelection;
    }

    /**
     * Returns if the item at the position is selected or not
     *
     * @param position the adapter position of the item
     * @return true if the item is slected, false otherwise
     */
    public boolean isSelected(int position){
        if (SINGLE_CHOICE == choiceMode) {
            return selectedPosition == position;
        } else {
            return selectedPositions.contains(position);
        }
    }

    /**
     * @return how many items are selected currently, for SINGLE_CHOICE it's 1 always
     *          if the selected position id not {@link Spinner#NO_POSITION},
     *          for MULTIPLE_CHOICE it is the number of valid selected positions
     */
    public int countSelection(){
        if(SINGLE_CHOICE == choiceMode){
            return 1;
        }
        else {
            return selectedPositions.size();
        }
    }

    /**
     * Dismiss the spinner dialog and clears the selection and set to default values
     */
    public void clearSelections(){
        dismissSpinnerDialog();
        selectedPositions.clear();
        selectedIds.clear();
        tmpSelectedPositions.clear();
        oldSelectedPosition = NO_POSITION;
        selectedPosition = NO_POSITION;
        oldSelectedId = NO_ID;
        selectedId = NO_ID;
        tmpOldSelectedPosition = NO_POSITION;
        tmpSelectedPosition = NO_POSITION;
        updateText();
    }

    /**
     * Sets the {@link SpinnerAdapter} to this Spinner.
     * The instance can be null. When the adapter is changed all the old selections are cleared and
     * the spinner dialog is dismissed if it was showing.
     *
     * @param newAdapter the new SpinnerAdapter
     */
    public void setAdapter(@Nullable SpinnerAdapter newAdapter){
        if (null != adapter){
            adapter.unregisterDataSetObserver(dataSetObserver);
        }
        clearSelections();
        adapter = newAdapter;
        if (null != newAdapter) {
            adapter.setSpinnerSelectionData(data);
            adapter.registerDataSetObserver(dataSetObserver);
        }
    }

    /**
     * @return the SpinnerAdapter attached to this Spinner
     */
    @Nullable
    public SpinnerAdapter getAdapter(){
        return adapter;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superParcelable = super.onSaveInstanceState();
        SavedState ss = new SavedState(superParcelable);
        ss.choiceMode = choiceMode;
        ss.isDialogShowing = isDialogShowing;
        if (SINGLE_CHOICE == choiceMode){
            ss.oldSelectedPosition = oldSelectedPosition;
            ss.oldSelectedId = oldSelectedId;
            ss.selectedPosition = selectedPosition;
            ss.selectedId = selectedId;
            if (isDialogShowing){
                ss.tmpOldSelectedPosition = tmpOldSelectedPosition;
                ss.tmpSelectedPosition = tmpSelectedPosition;
            }
        }
        else {
            ss.selectedPositions = selectedPositions;
            ss.selectedIds = selectedIds;
            if (isDialogShowing){
                ss.tmpSelectedPositions = tmpSelectedPositions;
            }
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        choiceMode = ss.choiceMode;
        isDialogShowing = ss.isDialogShowing;
        if (SINGLE_CHOICE == choiceMode){
            oldSelectedPosition = ss.oldSelectedPosition;
            oldSelectedId = ss.oldSelectedId;
            selectedPosition = ss.selectedPosition;
            selectedId = ss.selectedId;
            if (isDialogShowing){
                tmpOldSelectedPosition = ss.tmpOldSelectedPosition;
                tmpSelectedPosition = ss.tmpSelectedPosition;
            }
        }
        else {
            selectedPositions.addAll(ss.selectedPositions);
            selectedIds.addAll(ss.selectedIds);
            if (isDialogShowing){
                tmpSelectedPositions = ss.tmpSelectedPositions.clone();
            }
        }

        if (isDialogShowing){
            showSpinnerDialog();
        }
    }
    public  AlertDialog.Builder getDialogBuilder(){
        if(builder != null)
            return builder;
        else return null;
    }

    private Dialog buildSpinnerDialog(){
        View content = inflater.inflate(android.R.layout.list_content, null, false);
        list = content.findViewById(android.R.id.list);
        list.setOnItemClickListener(onItemClickListener);

        builder = new AlertDialog.Builder(getContext());
        builder.setView(content);
        builder.setTitle(dialogTitle);
        builder.setPositiveButton(dialogPositiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateSelection();

            }
        });
        builder.setNegativeButton(dialogNegativeText, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissSpinnerDialog();
            }
        });
        AlertDialog spinnerDialog = builder.create();
        spinnerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (list.getAdapter() != adapter) list.setAdapter(adapter);
            }
        });
        return spinnerDialog;
    }

    private void showSpinnerDialog(){
        if (null == spinnerDialog){
            spinnerDialog = buildSpinnerDialog();
        }
        spinnerDialog.show();
        isDialogShowing = true;
    }

    private void dismissSpinnerDialog(){
        if (null != spinnerDialog && spinnerDialog.isShowing()){
            spinnerDialog.show();
        }
        isDialogShowing = false;
    }

    private void updateSelection(){
        if (SINGLE_CHOICE == choiceMode){
            setSelected(tmpSelectedPosition, true);
        }
        else {
            selectedPositions.clear();
            selectedIds.clear();
            for (int i = 0; i < tmpSelectedPositions.size(); i++){
                if (tmpSelectedPositions.valueAt(i)){
                    int position = tmpSelectedPositions.keyAt(i);
                    selectedPositions.add(position);
                    selectedIds.add(adapter.getItemId(position));
                }
            }
            updateText();
        }
    }

    /**
     * Updates the item view with its new selection
     *
     * @param adapterPosition the adapter position of the item
     *
     * @see dreammaker.android.widget.SpinnerAdapter#updateView(int, android.view.View, boolean)
     */
    private void updateItemView(int adapterPosition){
        View itemView = getVisibleItemView(adapterPosition);
        boolean selected = data.isSelected(adapterPosition);
        if(null != itemView) adapter.updateView(adapterPosition, itemView, selected);
        if (SINGLE_CHOICE == choiceMode){
            View oldItemView = getVisibleItemView(tmpOldSelectedPosition);
            if (null != oldItemView) adapter.updateView(tmpOldSelectedPosition, oldItemView, false);
        }
    }

    @Nullable
    private View getVisibleItemView(int adapterPosition){
        int firstChildPosition = list.getFirstVisiblePosition();
        int childIndex = adapterPosition-firstChildPosition;
        if (childIndex >= 0 && childIndex < list.getChildCount()){
            return list.getChildAt(childIndex);
        }
        return null;
    }

    /**
     * Changes the EditText's text for the selected items. This method is called each time when
     * selection is changed.
     *
     * @see dreammaker.android.widget.SpinnerAdapter#getItemAsString(int)
     */
    private void updateText(){
        invalidate();
        if (SINGLE_CHOICE == choiceMode && NO_POSITION != selectedPosition){
            setText(adapter.getItemAsString(selectedPosition));
        }
        else if (MULTIPLE_CHOICE == choiceMode){
            StringBuilder builder = new StringBuilder();
            int c = 0;
            for (int position : selectedPositions) {
                builder.append(adapter.getItemAsString(position));
                if (c < selectedPositions.size() - 1) {
                    builder.append(", ");
                }
                c++;
            }
            this.setText(builder);
        }
    }

    /**
     * Class of some helper methods. This method is useful when someone want to hadle the item click manually
     * The methods of this class can only be used when the spinner dialog is showing, otherwise it will throws an runtime exception
     */
    public final class SpinnerSelectionData{

        /**
         * @return the current choice mode of the spinner
         */
        public int getChoiceMode(){
            checkDialogShowingOrThrow();
            return choiceMode;
        }

        /**
         * Sets the selection of the item at the given position
         *
         * @param adapterPosition the adapter position of the item
         * @param select the new selection
         */
        public void setSelected(int adapterPosition, boolean select){
            checkDialogShowingOrThrow();
            if (this.isSelected(adapterPosition) == select) return;
            if (SINGLE_CHOICE == choiceMode){
                if (select){
                    tmpOldSelectedPosition = tmpSelectedPosition;
                    tmpSelectedPosition = adapterPosition;
                }
            }
            else {
                tmpSelectedPositions.put(adapterPosition, select);
            }
            updateItemView(adapterPosition);
        }

        /**
         * Alters the selection if the item at the postion
         *
         * @param position adapter position of the item
         * @return the selection of the item
         */
        public boolean toggleSelection(int position){
            checkDialogShowingOrThrow();
            boolean newSelected = !this.isSelected(position);
            this.setSelected(position, newSelected);
            return newSelected;
        }

        /**
         * Returns the current selection of the item at position
         *
         * @param position the adapter position
         * @return true if selection, false otherwise
         */
        public boolean isSelected(int position){
            checkDialogShowingOrThrow();
            if (SINGLE_CHOICE == choiceMode){
                return tmpSelectedPosition == position;
            }
            else {
                return tmpSelectedPositions.get(position, false);
            }
        }

        /**
         * @return how many items are selected when the spinner dialog is showing,
         *          for SINGLE_CHOICE it is always 1 if the selected position
         *          is not {@link Spinner#NO_POSITION},
         *          for MULTIPLE_CHOICE it is the number of items checked true
         *
         */
        public int countSelection(){
            if (SINGLE_CHOICE == choiceMode){
                return tmpSelectedPosition != NO_POSITION ? 1 : 0;
            }
            else {
                return tmpSelectedPositions.size();
            }
        }

        private void checkDialogShowingOrThrow(){
            if (!isDialogShowing){
                throw new RuntimeException("this method can only be used when the spinner dialog is showing");
            }
        }
    }

    /**
     * A listener which is called for each items in the spinner dialog, when clicked
     */
    public interface OnSpinnerItemClickListener{

        /**
         * Called when the spinner dialog item is clicked
         *
         * @param spinner the Spinner object this listener is attached with
         * @param adapterPosition the position of the item in the adapter
         * @return true if the item click is handled by the implemented class, false otherwise.
         *          Remember that if true is returned from this method then the item selection is not handled by this class,
         *          the listening class must handle this item selection and visual updates of the spinner dialog items.
         *          For the purpose of manual updating selection and item views,
         *          use the {@link SpinnerSelectionData} class.
         *
         * @see SpinnerSelectionData
         */
        boolean onClickSpinnerItem(Spinner spinner, int adapterPosition);
    }

    public static class SavedState extends BaseSavedState{

        int choiceMode;
        List<Integer> selectedPositions;
        List<Long> selectedIds;
        SparseBooleanArray tmpSelectedPositions;
        int oldSelectedPosition;
        long oldSelectedId;
        int selectedPosition;
        long selectedId;
        int tmpOldSelectedPosition;
        int tmpSelectedPosition;
        boolean isDialogShowing;

        @NonNull
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcel source) {
            super(source);
            readFromParcel(source);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(choiceMode);
            out.writeInt(isDialogShowing ? 1 : 0);
            if (SINGLE_CHOICE == choiceMode){
                out.writeInt(oldSelectedPosition);
                out.writeLong(oldSelectedId);
                out.writeInt(selectedPosition);
                out.writeLong(selectedId);
                if (isDialogShowing){
                    out.writeInt(tmpOldSelectedPosition);
                    out.writeInt(tmpSelectedPosition);
                }
            }
            else {
                out.writeList(selectedPositions);
                out.writeList(selectedIds);
                if (isDialogShowing){
                    out.writeSparseBooleanArray(tmpSelectedPositions);
                }
            }
        }

        private void readFromParcel(Parcel src){
            choiceMode = src.readInt();
            isDialogShowing = src.readInt() == 1;
            if (SINGLE_CHOICE == choiceMode){
                oldSelectedPosition = src.readInt();
                oldSelectedId = src.readLong();
                selectedPosition = src.readInt();
                selectedId = src.readLong();
                if (isDialogShowing){
                    tmpOldSelectedPosition = src.readInt();
                    tmpSelectedPosition = src.readInt();
                }
            }
            else {
                selectedPositions = new LinkedList<>();
                selectedIds = new LinkedList<>();
                src.readList(selectedPositions, Integer.class.getClassLoader());
                src.readList(selectedIds, Long.class.getClassLoader());
                if (isDialogShowing){
                    tmpSelectedPositions = src.readSparseBooleanArray();
                }
            }
        }
    }

    private class AdapterDataSetObserver extends DataSetObserver{

        @Override
        public void onChanged() {
            super.onChanged();
            clearSelections();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            clearSelections();
        }
    }
}
