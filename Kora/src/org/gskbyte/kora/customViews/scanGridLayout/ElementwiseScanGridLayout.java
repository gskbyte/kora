package org.gskbyte.kora.customViews.scanGridLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

public class ElementwiseScanGridLayout extends ScanGridLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "ElementWiseScanGridLayout";
    
    private int mFocusIndex = 0;

    public ElementwiseScanGridLayout(Context context)
    {
        super(context);
    }
    
    public void start(int millisInterval)
    {
        super.start(millisInterval);
        mFocusIndex = 0;
        if(getChildCount()>0)
            getChildAt(0).requestFocusFromTouch();
    }

    public boolean isFocusOnFirstPosition()
    {
        int firstFocusableIndex = 0;
        
        while(!getChildAt(firstFocusableIndex).isFocusable() &&
              firstFocusableIndex<getChildCount())
        {
            ++firstFocusableIndex;
        }
        
        return mFocusIndex == firstFocusableIndex;
    }
    
    public boolean isFocusOnLastPosition()
    {
        int lastFocusableIndex = getChildCount();
        
        while(!getChildAt(lastFocusableIndex).isFocusable() &&
              lastFocusableIndex>=0)
        {
            --lastFocusableIndex;
        }
        
        return mFocusIndex == lastFocusableIndex;
    }
    
    public void resetFocus()
    {
        pauseTimer();
        
        mFocusIndex = 0;
        invalidate();
        
        continueTimer();
    }
    
    public void focusNext()
    {
        int prev = mFocusIndex; // para evitar bucles infinitos
        do{
            mFocusIndex = (mFocusIndex+1)%getChildCount();
        } while(!getChildAt(mFocusIndex).isFocusable() && mFocusIndex != prev);
        getChildAt(mFocusIndex).requestFocusFromTouch();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            View currentChild = getChildAt(mFocusIndex);
            currentChild.performClick();
        }
        
        return true;
    }
    
    @Override
    public void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        
        // Ocultar elementos de otras filas
        mBlackRegion.set(0, 0, getWidth(), getHeight());
        View currentChild = getChildAt(mFocusIndex);
        
        int startx = currentChild.getLeft(),
        starty = currentChild.getTop();
        
        mVisibleRect.set(startx, starty, startx+mColumnWidth, starty+mRowHeight);

        mBlackRegion.op(mVisibleRect, Region.Op.DIFFERENCE);
        canvas.drawPath(mBlackRegion.getBoundaryPath(), mPaint);
        
        invalidate();
    }
}
