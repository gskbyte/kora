package org.gskbyte.kora.customViews.koraSeekBar;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class KoraSeekBar extends LinearLayout
{
    protected static final int DEFAULT_NSTEPS = 10;
    
    protected SeekBar mSeekBar;
    protected TextView mValueText;
    
    protected int mSteps;
    
    public KoraSeekBar(Context context)
    {
        this(context, DEFAULT_NSTEPS);
    }
    
    public KoraSeekBar(Context context, int nsteps)
    {
        super(context);
        
        LayoutInflater.from(context).inflate(R.layout.seekbar, this, true);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mValueText = (TextView) findViewById(R.id.value);

        mSteps = nsteps;
        mSeekBar.setMax(mSteps-1);
    }
    
    public KoraSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        LayoutInflater.from(context).inflate(R.layout.seekbar, this, true);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mValueText = (TextView) findViewById(R.id.value);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, 
                                                          R.styleable.KoraSeekBar, 
                                                          0, 0);
            mSteps = a.getInt(R.styleable.KoraSeekBar_steps, DEFAULT_NSTEPS);
            
            a.recycle();
        } else {
            mSteps = DEFAULT_NSTEPS;
        }
        mSeekBar.setMax(mSteps-1);
    }
    
    public String getText()
    {
        return mValueText.getText().toString();
    }
    
    public int getIndex()
    {
        return mSeekBar.getProgress();
    }
    
    public int getNumSteps()
    {
        return mSteps;
    }
    
    public void setNumSteps(int nteps)
    {
        mSeekBar.setMax(mSteps-1);
    }
}
