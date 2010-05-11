package org.gskbyte.kora.customViews.scanGridLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

public class RowColumnScanGridLayout extends ScanGridLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "RowColumnScanGridLayout";
    
    private int mCurrentRow = 0, mCurrentColumn = 0;
    private int mNumFilledRows, mNumFilledColumns;
    
    private boolean mRowChosen = false;
    private int mCurrentColumnCycle = 0, mMaxColumnCycles;
    
    private long mLastEventTime = 0;

    public RowColumnScanGridLayout(Context context)
    {
        super(context);
    }
    
    public void start(int millisInterval)
    {
        start(millisInterval, 2);
    }
    
    public void start(int millisInterval, int maxColumnCycles)
    {
        super.start(millisInterval);
        mNumFilledRows = getChildCount() / mNumColumns;
        if(mNumFilledRows*mNumColumns<getChildCount())
            ++mNumFilledRows;
        mMaxColumnCycles = maxColumnCycles;
        
        mCurrentRow = mCurrentColumn = 0;
        mRowChosen = false;
        mCurrentColumnCycle = 0;
        
        invalidate();
    }
    
    public boolean isFocusOnFirstPosition()
    {
        int firstFocusableIndex = 0;
        
        while(!getChildAt(firstFocusableIndex).isFocusable() &&
              firstFocusableIndex<getChildCount())
        {
            ++firstFocusableIndex;
        }
        
        return (mCurrentRow*mNumColumns+mCurrentColumn) == firstFocusableIndex;
    }
    
    public boolean isFocusOnLastPosition()
    {
        int lastFocusableIndex = getChildCount();
        
        while(!getChildAt(lastFocusableIndex).isFocusable() &&
              lastFocusableIndex>=0)
        {
            --lastFocusableIndex;
        }
        
        return (mCurrentRow*mNumColumns+mCurrentColumn) == lastFocusableIndex;
    }

    public void resetFocus()
    {
        pauseTimer();
        
        mCurrentRow = mCurrentColumn = 0;
        mRowChosen = false;
        mCurrentColumnCycle = 0;
        invalidate();
        
        continueTimer();
    }
    
    @Override
    public void focusNext()
    {
        if(getChildCount()>0){
            if(!mRowChosen){
                clearFocus();
                mCurrentRow = (mCurrentRow+1)%mNumFilledRows;
                if(mCurrentRow<mNumFilledRows){
                    mNumFilledColumns = mNumColumns;
                } else {
                    mNumFilledColumns = getChildCount() -
                                        (mCurrentRow-1)*mNumColumns;
                }
            } else {
                int prev = mCurrentColumn; // para evitar bucles infinitos
                int startcolumn = mCurrentRow*mNumColumns;
                do{
                    mCurrentColumn = (mCurrentColumn+1)%mNumFilledColumns;
                    if(mCurrentColumn == 0)
                        ++mCurrentColumnCycle;
                    if(mCurrentColumnCycle == mMaxColumnCycles)
                        mRowChosen = false;
                } while(!getChildAt(startcolumn+mCurrentColumn).isFocusable()
                        && mCurrentColumn != prev);
                getChildAt(startcolumn+mCurrentColumn).requestFocusFromTouch();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        
        if(event.getAction() == MotionEvent.ACTION_DOWN &&
           event.getEventTime()-mLastEventTime>50){
            mLastEventTime = event.getEventTime();
            
            pauseTimer();
            
            if(mRowChosen){
                View currentChild =
                    getChildAt(mCurrentRow*mNumColumns+mCurrentColumn);
                currentChild.performClick();
            } else {
                mCurrentColumnCycle = 0;
                mCurrentColumn = 0;
                mRowChosen = !mRowChosen;
                int startcolumn = mCurrentRow*mNumColumns;
                // por si el primer elemento no fuera focusable (vista vacía)
                while(!getChildAt(startcolumn+mCurrentColumn).isFocusable()){
                    ++mCurrentColumn;
                }
                getChildAt(startcolumn+mCurrentColumn).requestFocusFromTouch();
                continueTimer();
            }
            
        }
        return true;
    }
    
    @Override
    public void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        
        mBlackRegion.set(0, 0, getWidth(), getHeight());
        int starty = (mCurrentRow) * (mRowHeight + mMargin);
        
        if(mRowChosen){ // Ocultar resto de elementos de la fila
            int startx = (mCurrentColumn) * (mColumnWidth + mMargin);
            mVisibleRect.set(startx, starty, startx+mColumnWidth, starty+mRowHeight);
        } else { // ocultar resto de columnas
            mVisibleRect.set(0, starty, getWidth(), starty+mRowHeight);
        }

        mBlackRegion.op(mVisibleRect, Region.Op.DIFFERENCE);
        canvas.drawPath(mBlackRegion.getBoundaryPath(), mPaint);
        
        invalidate();
    }

}
