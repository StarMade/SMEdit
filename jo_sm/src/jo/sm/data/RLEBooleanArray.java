package jo.sm.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RLEBooleanArray implements Iterable<Integer>
{
    private List<int[]> mCells;
    
    public RLEBooleanArray()
    {
        mCells = new ArrayList<>();
    }
    
    public Iterator<Integer> iterator()
    {
        return new RLEBooleanArrayIterator();
    }
    
    public boolean isEmpty()
    {
        return mCells.size() == 0;
    }
    
    public boolean get(int idx)
    {
        CellHit hit = find(idx);
        if (hit.mTargetFound)
            return true;
        else
            return false;
    }
    
    public void set(int idx, boolean val)
    {
        CellHit hit = find(idx);
        if (hit.mTargetFound)
        {
            if (!val)
            {
                setFalse(idx, hit);
            }
        }
        else
        {
            if (val)
            {
                setTrue(idx, hit);
            }
        }
    }

    private void setTrue(int idx, CellHit hit)
    {
        if (hit.mLowCellIdx < 0)
            if (hit.mHighCellIdx < 0)
            {   // neither high nor low, therefore empty
                mCells.add(new int[] { idx, idx });
            }
            else
            {   // high but no low
                if (hit.mHighCell[0] - 1 == idx)
                {   // merge
                    mCells.remove(hit.mHighCellIdx);
                    mCells.add(hit.mHighCellIdx, new int[] { idx, hit.mHighCell[1] });
                }
                else
                {   // insert
                    mCells.add(hit.mHighCellIdx, new int[] { idx, idx });
                }
            }
        else
            if (hit.mHighCellIdx < 0)
            {   // low but no high
                if (hit.mLowCell[1] + 1 == idx)
                {   // merge
                    mCells.remove(hit.mLowCellIdx);
                    mCells.add(hit.mLowCellIdx, new int[] { hit.mLowCell[0], idx });
                }
                else
                {   // insert
                    mCells.add(hit.mLowCellIdx + 1, new int[] { idx, idx });
                }
            }
            else
            {   // high and low
                if (hit.mLowCell[1] + 1 == idx)
                {   // adjacent to low
                    if (hit.mHighCell[0] - 1 == idx)
                    {   // adjacent to high
                        // merge high and low
                        mCells.remove(hit.mHighCellIdx);
                        mCells.remove(hit.mLowCellIdx);
                        mCells.add(hit.mLowCellIdx, new int[] { hit.mLowCell[0], hit.mHighCell[1] });
                    }
                    else
                    {   // not adjacent to high
                        // merge into low
                        mCells.remove(hit.mLowCellIdx);
                        mCells.add(hit.mLowCellIdx, new int[] { hit.mLowCell[0], idx });
                    }
                }
                else
                {   // not adjacent to low
                    if (hit.mHighCell[0] - 1 == idx)
                    {   // adjacent to high
                        // merge into high
                        mCells.remove(hit.mHighCellIdx);
                        mCells.add(hit.mHighCellIdx, new int[] { idx, hit.mHighCell[1] });
                    }
                    else
                    {   // not adjacent to high
                        // create new
                        mCells.add(hit.mHighCellIdx, new int[] { idx, idx });
                    }
                }
            }
    }

    private void setFalse(int idx, CellHit hit)
    {
        if (hit.mTargetCell[0] == idx)
        {   // is low bound
            if (hit.mTargetCell[1] == idx)
            {   // is high bound
                // remove
                mCells.remove(hit.mTargetCellIdx);
            }
            else
            {   // isn't high bound
                // shrink up
                mCells.remove(hit.mTargetCellIdx);
                mCells.add(hit.mTargetCellIdx, new int[] { hit.mTargetCell[0]+1, hit.mTargetCell[1]});
            }
        }
        else
        {   // isn't low bound
            if (hit.mTargetCell[1] == idx)
            {   // is high bound
                // shrink down
                mCells.remove(hit.mTargetCellIdx);
                mCells.add(hit.mTargetCellIdx, new int[] { hit.mTargetCell[0], hit.mTargetCell[1]-1});
            }
            else
            {   // isn't high bound
                // split
                mCells.remove(hit.mTargetCellIdx);
                mCells.add(hit.mTargetCellIdx, new int[] { hit.mTargetCell[0], idx-1});
                mCells.add(hit.mTargetCellIdx+1, new int[] { idx+1, hit.mTargetCell[1]});
            }
        }
    }
    
    private CellHit find(int targetValue)
    {
        CellHit hit = new CellHit();
        if (setupInitial(hit, targetValue))
            return hit;
        for (;;)
        {
            if (hit.mLowCellIdx + 1 == hit.mHighCellIdx)
            {
                hit.mTargetFound = false;
                return hit;
            }
            int midCellIdx = (hit.mLowCellIdx + hit.mHighCellIdx)/2;
            int[] midCell = mCells.get(midCellIdx);
            if (targetValue < midCell[0])
            {
                hit.mHighCellIdx = midCellIdx;
                hit.mHighCell = midCell;
            }
            else if (targetValue > midCell[1])
            {
                hit.mLowCellIdx = midCellIdx;
                hit.mLowCell = midCell;
            }
            else
            {
                hit.mTargetCellIdx = midCellIdx;
                hit.mTargetCell = midCell;
                hit.mTargetFound = true;
                return hit;
            }
        }
    }
    
    private boolean setupInitial(CellHit hit, int targetValue)
    {
        hit.mTargetValue = targetValue;
        if (mCells.size() == 0)
        {
            hit.mTargetFound = false;
            hit.mLowCellIdx = -1;
            hit.mHighCellIdx = -1;
            return true;
        }
        hit.mLowCellIdx = 0;
        hit.mLowCell = mCells.get(hit.mLowCellIdx);
        if (hit.mLowCell[0] > targetValue)
        {   // target is lower than low
            hit.mTargetFound = false;
            hit.mHighCellIdx = hit.mLowCellIdx;
            hit.mHighCell = hit.mLowCell;
            hit.mLowCellIdx = -1;
            hit.mLowCell = null;
            return true;
        }
        if (hit.mLowCell[1] >= targetValue)
        {   // target is in low range
            hit.mTargetFound = true;
            hit.mTargetCellIdx = hit.mLowCellIdx;
            hit.mTargetCell = hit.mLowCell;
            hit.mLowCellIdx = -1;
            hit.mLowCell = null;
            return true;
        }
        hit.mHighCellIdx = mCells.size() - 1;
        hit.mHighCell = mCells.get(hit.mHighCellIdx);
        if (hit.mHighCell[1] < targetValue)
        {   // target is higher than high
            hit.mTargetFound = false;
            hit.mLowCellIdx = hit.mHighCellIdx;
            hit.mLowCell = hit.mHighCell;
            hit.mHighCellIdx = -1;
            hit.mHighCell = null;
            return true;
        }
        if (hit.mLowCell[0] <= targetValue)
        {   // target is in high range
            hit.mTargetFound = true;
            hit.mTargetCellIdx = hit.mHighCellIdx;
            hit.mTargetCell = hit.mHighCell;
            hit.mHighCellIdx = -1;
            hit.mHighCell = null;
            return true;
        }
        return false;
    }
    
    class CellHit
    {
        public int mTargetValue;
        public boolean mTargetFound;
        public int mTargetCellIdx;
        public int mLowCellIdx;
        public int mHighCellIdx;
        public int[] mTargetCell;
        public int[] mLowCell;
        public int[] mHighCell;
    }
    
    class RLEBooleanArrayIterator implements Iterator<Integer>
    {
        private int mAtValue;
        private int mAtCell;
        private int mHighValue;
        
        public RLEBooleanArrayIterator()
        {
            if (mCells.size() == 0)
            {
                mAtValue = 0;
                mAtCell = -1;
                mHighValue = -1;
            }
            else
            {
                mAtCell = 0;
                mAtValue = mCells.get(mAtCell)[0];
                mHighValue = mCells.get(mCells.size() - 1)[1];
            }
        }

        @Override
        public boolean hasNext()
        {
            return mAtValue <= mHighValue;
        }

        @Override
        public Integer next()
        {
            int ret = mAtValue;
            mAtValue++;
            if (mAtValue <= mHighValue)
            {
                int[] atCell = mCells.get(mAtCell);
                if (mAtValue > atCell[1])
                {
                    mAtCell++;
                    atCell = mCells.get(mAtCell);
                    mAtValue = atCell[0];
                }
            }
            return ret;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("Not implemented");
        }
        
    }
}

