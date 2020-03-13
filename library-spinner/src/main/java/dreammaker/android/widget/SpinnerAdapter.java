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

import android.view.View;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

/**
 * A {@link ListAdapter} for the spinner items
 */
public interface SpinnerAdapter extends ListAdapter {

    /**
     * Returns the CharSequence representation of the adapter item at the position
     *
     * @param position the adapter position of the item
     * @return the CharSequence of the of the item
     */
    CharSequence getItemAsString(int position);

    /**
     * The {@link dreammaker.android.widget.Spinner.SpinnerSelectionData}
     * to attach with this adapter
     *
     * @param data the SpinnerSelectionData instance
     */
    void setSpinnerSelectionData(@NonNull Spinner.SpinnerSelectionData data);

    /**
     * The {@link dreammaker.android.widget.Spinner.SpinnerSelectionData}
     * instance attached with this adapter
     *
     * @return the SpinnerSelectionData instance
     *
     * @see dreammaker.android.widget.Spinner.SpinnerSelectionData
     */
    @NonNull
    Spinner.SpinnerSelectionData getSpinnerSelectionData();

    /**
     * Update the selection of the dialog item view at the position.
     *
     * @param position the adapter position of the item
     * @param itemView the itemView to update
     * @param isSelected the new selection of the view
     */
    void updateView(int position, @NonNull View itemView, boolean isSelected);

    /**
     * Returns the adapter position of the item with the given itemId
     *
     * @param itemId the id of the item to find the position
     * @return the adapter position of the item
     */
    int getPositionForId(long itemId);
}
