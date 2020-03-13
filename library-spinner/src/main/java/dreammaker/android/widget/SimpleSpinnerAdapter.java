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
import android.widget.Checkable;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * A simple {@link SimpleAdapter} implementation
 * of {@link SpinnerAdapter}
 *
 * @see android.widget.SimpleAdapter
 * @see android.widget.SpinnerAdapter
 */
public class SimpleSpinnerAdapter extends SimpleAdapter implements SpinnerAdapter {

    private Spinner.SpinnerSelectionData data;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public SimpleSpinnerAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
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
