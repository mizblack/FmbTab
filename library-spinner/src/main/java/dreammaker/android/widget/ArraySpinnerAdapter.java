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

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Checkable;

import java.util.List;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * A simple {@link ArrayAdapter} implementation of
 * {@link SpinnerAdapter}
 *
 * @param <T> item type of this adapter
 *
 * @see android.widget.ArrayAdapter
 * @see android.widget.SpinnerAdapter
 */
public class ArraySpinnerAdapter<T> extends ArrayAdapter<T> implements SpinnerAdapter {

    private Spinner.SpinnerSelectionData data;

    public ArraySpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ArraySpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ArraySpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    public ArraySpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ArraySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public ArraySpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public static ArraySpinnerAdapter<CharSequence> createFromResource(@NonNull Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId){
        final CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ArraySpinnerAdapter<CharSequence>(context, textViewResId, 0, strings){};
    }

    @Override
    public void setSpinnerSelectionData(@NonNull Spinner.SpinnerSelectionData data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Spinner.SpinnerSelectionData getSpinnerSelectionData() {
        return data;
    }

    @Override
    public CharSequence getItemAsString(int position) {
        return String.valueOf(getItem(position));
    }

    @Override
    public void updateView(int position, @NonNull View itemView, boolean isSelected) {
        if (itemView instanceof Checkable){
            ((Checkable) itemView).setChecked(isSelected);
        }
        else {
            itemView.setSelected(isSelected);
        }
    }

    @Override
    public int getPositionForId(long itemId) {
        return SpinnerAdapterHelper.getPositionForId(this, itemId);
    }
}
