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

package com.eye3.golfpay.fmb_tab.common;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

import com.eye3.golfpay.fmb_tab.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdapterDataProvider {

    public static List<String> getListOfStringItems(int count){
        LinkedList<String> items = new LinkedList<>();
        for (int i = 0; i < count; i++){
            items.add(String.format("Item %2d", i));
        }
        return items;
    }

    public static Cursor getCursorOfItems(int count){
        MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "item"});
        for (int i = 0; i < count; i++){
            cursor.addRow(new Object[]{i+1, String.format("Item %02d", i)});
        }
        return cursor;
    }

//    public static List<String> getListOfFruits(){
//        ArrayList<String> fruits = new ArrayList<>();
//        fruits.add("Mango");
//        fruits.add("Litchi");
//        fruits.add("Straw Berry");
//        fruits.add("Blue Berry");
//        fruits.add("Guava");
//        fruits.add("Banana");
//        fruits.add("Grapes");
//        fruits.add("Water Mellon");
//
//        return fruits;
//    }


    public static List<String> getGuestList(){
        List<String> ArrList = new ArrayList<>();
        for(int i=0; AppDef.orderDetailList.size() > i ;i++){
            ArrList.add(OrderFragment.getGuestName(AppDef.orderDetailList.get(i).reserve_guest_id));
        }
        return ArrList;
    }

}
