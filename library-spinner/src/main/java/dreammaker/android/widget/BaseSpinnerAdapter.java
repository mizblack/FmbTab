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

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

/**
 * A simple {@link BaseAdapter} implementation
 * of {@link SpinnerAdapter}
 *
 * @see android.widget.ArrayAdapter
 * @see android.widget.SpinnerAdapter
 */
public abstract class BaseSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Spinner.SpinnerSelectionData data;

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
    public int getPositionForId(long itemId) {
        return SpinnerAdapterHelper.getPositionForId(this, itemId);
    }
}
