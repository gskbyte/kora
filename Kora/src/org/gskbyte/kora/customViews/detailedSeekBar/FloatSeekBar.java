package org.gskbyte.kora.customViews.detailedSeekBar;

import java.text.DecimalFormat;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FloatSeekBar extends AbstractSeekBar
{
    protected static final float DEFAULT_MIN = 0.0f,
                                 DEFAULT_MAX = 1.0f;
    protected static final int DEFAULT_DECIMALS = 2;
    
    protected float mMinimum, mMaximum;
    protected DecimalFormat mDecimalFormat = new DecimalFormat("0.00"); 

    public FloatSeekBar(Context context)
    {
        this(context, DEFAULT_MIN, DEFAULT_MAX, DEFAULT_NSTEPS);
    }
    
    public FloatSeekBar(Context context, float min, float max)
    {
        this(context, min, max, (int)(max-min+1));
    }
    
    public FloatSeekBar(Context context, float min, float max, int nsteps)
    {
        super(context, nsteps);
        mMinimum = min;
        mMaximum = max;

        mSeekBar.setOnSeekBarChangeListener(seekListener);
        mValueText.setText(String.valueOf(mMinimum) );
    }
    
    public FloatSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, 
                                                          R.styleable.FloatSeekBar, 
                                                          0, 0);
            mMinimum = a.getFloat(R.styleable.FloatSeekBar_minValue, DEFAULT_MIN);
            mMaximum = a.getFloat(R.styleable.FloatSeekBar_maxValue, DEFAULT_MAX);
            a.recycle();
            
            a = context.obtainStyledAttributes(attrs, R.styleable.DetailedSeekBar, 
                                               0, 0);
            
            mSteps = a.getInt(R.styleable.DetailedSeekBar_steps, (int)(mMaximum-mMinimum+1));
            a.recycle();
        } else {
            mMinimum = DEFAULT_MIN;
            mMaximum = DEFAULT_MAX;
            mSteps = DEFAULT_NSTEPS;
        }
        
        mSeekBar.setMax(mSteps-1);
        mSeekBar.setOnSeekBarChangeListener(seekListener);
        mValueText.setText(String.valueOf(mMinimum) );
        mValueText.setText( mDecimalFormat.format(mMinimum) );
    }
    
    public float getValue()
    {
        return mMinimum +
               mSeekBar.getProgress()*((mMaximum-mMinimum)/(mSteps-1));
    }
    
    public void setValue(float i)
    {
        if(i>=mMinimum && i<=mMaximum){
            float v = (i-mMinimum) / ((mMaximum-mMinimum)/(mSteps-1));
            mSeekBar.setProgress( (int)v );
        }
    }
    
    public float getMinimum()
    {
        return mMinimum;
    }
    
    public float getMaximum()
    {
        return mMaximum;
    }
    
    private OnSeekBarChangeListener seekListener =
        new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser)
            {
                int v = mSeekBar.getProgress();
                float value = mMinimum + (v*(mMaximum-mMinimum)/(mSteps-1));
                
                mValueText.setText( mDecimalFormat.format(value) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        
    };

}
