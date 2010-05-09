package org.gskbyte.kora.customViews.scanGridLayout;

import org.gskbyte.kora.customViews.GridLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public abstract class ScanGridLayout extends GridLayout
{
    private class Timer implements Runnable
    {
        long millis;
        
        public void run(){
            ScanGridLayout.this.mHandler.postDelayed(this, millis);
            ScanGridLayout.this.focusNext();
        }
    }
    
    protected final android.os.Handler mHandler = new android.os.Handler();
    protected final Timer mTimer = new Timer();
    
    public ScanGridLayout(Context context)
    {
        super(context);
    }

    public ScanGridLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScanGridLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public void start(long millisInterval)
    {
        mTimer.millis = millisInterval;
        mHandler.post(mTimer);
    }
    
    public void stop()
    {
        mHandler.removeCallbacks(mTimer);
    }
    
    public abstract void focusNext();
    
    public abstract boolean onTouchEvent(MotionEvent event);
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return true;
    } 
}
