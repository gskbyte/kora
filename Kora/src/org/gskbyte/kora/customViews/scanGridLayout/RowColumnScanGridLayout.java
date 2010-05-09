package org.gskbyte.kora.customViews.scanGridLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class RowColumnScanGridLayout extends ScanGridLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "RowColumnScanGridLayout";
    
    private int mCurrentRow = 0, mCurrentColumn = 0;
    private int mNumFilledRows, mNumFilledColumns;
    private boolean mRowChosen = false;

    public RowColumnScanGridLayout(Context context)
    {
        super(context);
    }
    
    public void start(long millisInterval)
    {
        super.start(millisInterval);
        mNumFilledRows = getChildCount() / mNumColumns;
        if(mNumFilledRows*mNumColumns<getChildCount())
            ++mNumFilledRows;
        invalidate();
    }
    
    @Override
    public void focusNext()
    {
        if(!mRowChosen){
            mCurrentRow = (mCurrentRow+1)%mNumFilledRows;
            if(mCurrentRow<mNumFilledRows){
                mNumFilledColumns = mNumColumns;
            } else {
                mNumFilledColumns = getChildCount() -
                                    (mCurrentRow-1)*mNumColumns;
            }
        } else {
            mCurrentColumn = (mCurrentColumn+1)%mNumFilledColumns;
        }
        
        getChildAt(mCurrentRow*mNumColumns+mCurrentColumn).requestFocusFromTouch();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            View currentChild =
                getChildAt(mCurrentRow*mNumColumns+mCurrentColumn);
            //currentChild.performClick();
            mRowChosen = !mRowChosen;
            mCurrentColumn = 0;
            getChildAt(mCurrentRow*mNumColumns).requestFocusFromTouch();
        }
        return true;
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        
        // Dibujar selector sobre fila
    }

}
