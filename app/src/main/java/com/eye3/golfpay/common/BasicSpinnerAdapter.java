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

package com.eye3.golfpay.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eye3.golfpay.R;

import java.util.List;

import dreammaker.android.widget.ArraySpinnerAdapter;

public class BasicSpinnerAdapter extends ArraySpinnerAdapter<String> {

    public BasicSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, R.id.text1, objects);
    }

    @Override
    public void updateView(int position, @NonNull View itemView, boolean isSelected) {
        Checkable checkable = itemView.findViewById(R.id.checkable);
        checkable.setChecked(isSelected);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);
        Checkable checkable = itemView.findViewById(R.id.checkable);
        checkable.setChecked(getSpinnerSelectionData().isSelected(position));
        return itemView;
    }
}
