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

import android.database.Cursor;
import android.widget.Adapter;

/**
 * Class of some helper method. This class is for library use only.
 */
class SpinnerAdapterHelper {

    /**
     * Moves the cursor's position to the given position or throws exception if it fails to do so.
     *
     * @param cursor the {@link Cursor} instance
     * @param position the new position to move
     */
    static void moveCursorOrThrow(Cursor cursor, int position){
        if (null == cursor) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
    }

    /**
     * Finds the adapter position for the given item id
     *
     * @param adapter the spinner adapter
     * @param itemId the id
     * @return the adapter position of the item
     */
    static int getPositionForId(Adapter adapter, long itemId){
        for (int i = 0; i < adapter.getCount(); i++){
            if (itemId == adapter.getItemId(i)){
                return i;
            }
        }
        return -1;
    }
}
