package org.gskbyte.kora.customViews.scanGridLayout;

import org.gskbyte.kora.customViews.GridLayout;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

public abstract class ScanGridLayout extends GridLayout
{
    public interface FocusChangeListener
    {
        void focusChanged(ScanGridLayout source);
    }
    
    public interface FocusCycleListener
    {
        void focusCycled(ScanGridLayout source);
    }
    
    private abstract class Timer
    {
        private long mCountdownInterval;
        private boolean first;

        public synchronized final void start(long countDownInterval)
        {
            mCountdownInterval = countDownInterval;
            first = true;
            mHandler.sendMessage(mHandler.obtainMessage(1));
        }
        
        public final void cancel() {
            mHandler.removeMessages(1);
        }

        public abstract void onTick();

        private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                
                synchronized(Timer.this){
                    if(first)
                        first = false;
                    else
                        onTick();
                    sendMessageDelayed(obtainMessage(1), mCountdownInterval);
                }
            }
        };
    }

    
    protected long mMillisInterval;
    protected final Timer mTimer = new Timer(){
            @Override
            public void onTick()
            {
                focusNext();
                if(mFocusChangeListener != null)
                    mFocusChangeListener.focusChanged(ScanGridLayout.this);
                if(isFocusOnFirstPosition() && mFocusCycleListener != null)
                    mFocusCycleListener.focusCycled(ScanGridLayout.this);
                
            }
        };
    
    protected final Paint mPaint = new Paint();
    protected final Region mBlackRegion = new Region();
    protected final Rect mVisibleRect = new Rect();
    protected FocusChangeListener mFocusChangeListener;
    protected FocusCycleListener mFocusCycleListener;
    
    public ScanGridLayout(Context context)
    {
        super(context);
        mPaint.setColor(0xbb000000); // negro
    }

    public ScanGridLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScanGridLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mPaint.setColor(0xaa000000); // negro
    }
    
    public void start(int millisInterval)
    {
        mMillisInterval = millisInterval;
        mTimer.start(millisInterval);
    }
    
    public void pauseTimer()
    {
        if(mTimer != null){
            mTimer.cancel();
        }
    }
    
    public void continueTimer()
    {
        if(mTimer != null)
            mTimer.start(mMillisInterval);
    }
    
    public void setOnFocusChangeListener(FocusChangeListener l)
    {
        mFocusChangeListener = l;
    }
    
    public void setOnFocusCycleListener(FocusCycleListener l)
    {
        mFocusCycleListener = l;
    }
    
    public abstract boolean isFocusOnFirstPosition();
    public abstract boolean isFocusOnLastPosition();
    
    public abstract void resetFocus();
    public abstract void focusNext();
    
    public abstract boolean onTouchEvent(MotionEvent event);
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return true;
    } 
}
