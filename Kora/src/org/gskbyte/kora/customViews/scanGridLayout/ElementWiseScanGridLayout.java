package org.gskbyte.kora.customViews.scanGridLayout;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class ElementWiseScanGridLayout extends ScanGridLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "ElementWiseScanGridLayout";
    
    private int mFocusIndex = 0;

    public ElementWiseScanGridLayout(Context context)
    {
        super(context);
    }
    
    public void start(long millisInterval)
    {
        super.start(millisInterval);
        getChildAt(0).requestFocusFromTouch();
        invalidate();
    }
    
    public void stop()
    {
        mHandler.removeCallbacks(mTimer);
    }
    
    public void focusNext()
    {
        mFocusIndex = (mFocusIndex+1)%getChildCount();
        getChildAt(mFocusIndex).requestFocusFromTouch();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mHandler.removeCallbacks(mTimer);
            View currentChild = getChildAt(mFocusIndex);
            currentChild.performClick();
        }
        
        return true;
    }
}
