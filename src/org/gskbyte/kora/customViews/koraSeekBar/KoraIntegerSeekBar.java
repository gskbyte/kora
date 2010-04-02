package org.gskbyte.kora.customViews.koraSeekBar;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class KoraIntegerSeekBar extends KoraSeekBar
{
    protected static final int DEFAULT_MIN = 0,
                               DEFAULT_MAX = 9;

    protected int mMinimum, mMaximum;
    
    public KoraIntegerSeekBar(Context context)
    {
        this(context, DEFAULT_MIN, DEFAULT_MAX, DEFAULT_NSTEPS);
    }
    
    public KoraIntegerSeekBar(Context context, int min, int max)
    {
        this(context, min, max, max-min+1);
    }
    
    public KoraIntegerSeekBar(Context context, int min, int max, int nsteps)
    {
        super(context, nsteps);
        mMinimum = min;
        mMaximum = max;

        mSeekBar.setOnSeekBarChangeListener(seekListener);
        mValueText.setText(String.valueOf(mMinimum) );
    }
    
    public KoraIntegerSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, 
                                                          R.styleable.KoraIntegerSeekBar, 
                                                          0, 0);
            mMinimum = a.getInt(R.styleable.KoraIntegerSeekBar_minimum, DEFAULT_MIN);
            mMaximum = a.getInt(R.styleable.KoraIntegerSeekBar_maximum, DEFAULT_MAX);
            a.recycle();
            
            a = context.obtainStyledAttributes(attrs, R.styleable.KoraSeekBar, 
                                               0, 0);
            mSteps = a.getInt(R.styleable.KoraSeekBar_steps, mMaximum-mMinimum+1);
            a.recycle();
        } else {
            mMinimum = DEFAULT_MIN;
            mMaximum = DEFAULT_MAX;
            mSteps = DEFAULT_NSTEPS;
        }
        
        mSeekBar.setMax(mSteps-1);
        mSeekBar.setOnSeekBarChangeListener(seekListener);
        mValueText.setText(String.valueOf(mMinimum) );
    }
    
    public int getValue()
    {
        return mMinimum + mSeekBar.getProgress();
    }
    
    public int getMinimum()
    {
        return mMinimum;
    }
    
    public int getMaximum()
    {
        return mMaximum;
    }
    
    public void setRange(int min, int max)
    {
        int prevvalue = mSeekBar.getProgress(),
            prevmax = mSeekBar.getMax();
        
        mMinimum = min;
        mMaximum = max;
        
        mSeekBar.setProgress( ((prevvalue<<10)/(prevmax))>>10 );
    }
    
    private OnSeekBarChangeListener seekListener =
        new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser)
            {
                int v = mSeekBar.getProgress();
                mValueText.setText( String.valueOf( mMinimum + ((v*((mMaximum-mMinimum)<<10)/(mSteps-1))>>10) ) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
                
            }
        
    };

}
