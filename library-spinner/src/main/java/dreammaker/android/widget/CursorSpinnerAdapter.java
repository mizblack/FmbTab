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
import android.database.Cursor;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;

/**
 * A simple {@link CursorAdapter} implementation
 * of {@link SpinnerAdapter}
 *
 * @see androidx.cursoradapter.widget.CursorAdapter
 * @see android.widget.SpinnerAdapter
 */
public abstract class CursorSpinnerAdapter extends CursorAdapter implements SpinnerAdapter {

    private Spinner.SpinnerSelectionData data;

    @Deprecated
    public CursorSpinnerAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public CursorSpinnerAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public CursorSpinnerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
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
    public void updateView(int position, @NonNull View itemView, boolean isSelected) {
        SpinnerAdapterHelper.moveCursorOrThrow(getCursor(), position);
        bindView(itemView, itemView.getContext(), getCursor());
    }

    @Override
    public int getPositionForId(long itemId) {
        return SpinnerAdapterHelper.getPositionForId(this, itemId);
    }
}
