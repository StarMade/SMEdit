/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.data;

import java.util.ArrayList;
import java.util.List;

import jo.sm.logic.GridLogic;
import jo.sm.ship.data.Block;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class UndoBuffer {

    private final List<byte[]> mBuffer;
    private int mPointer;

    public UndoBuffer() {
        mBuffer = new ArrayList<>();
        mPointer = 0;
    }

    public SparseMatrix<Block> undo() {
        if (mPointer > 0) {
            mPointer--;
            return GridLogic.fromBytes(mBuffer.get(mPointer));
        } else {
            return null;
        }
    }

    public SparseMatrix<Block> redo() {
        if (mPointer < mBuffer.size()) {
            return GridLogic.fromBytes(mBuffer.get(mPointer++));
        } else {
            return null;
        }
    }

    public void checkpoint(SparseMatrix<Block> grid) {
        if (grid.size() > 10000) {
            return;
        }
        while (mBuffer.size() > mPointer) {
            mBuffer.remove(mPointer);
        }
        mBuffer.add(GridLogic.toBytes(grid));
        mPointer++;
    }

    public void clear() {
        mBuffer.clear();
    }
}
