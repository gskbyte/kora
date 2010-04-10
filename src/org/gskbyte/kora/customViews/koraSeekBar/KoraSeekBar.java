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
    
    protected TextView mTitleText;
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

        mTitleText = (TextView) findViewById(R.id.title);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mValueText = (TextView) findViewById(R.id.value);

        mSteps = nsteps;
        mSeekBar.setMax(mSteps-1);
    }
    
    public KoraSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        LayoutInflater.from(context).inflate(R.layout.seekbar, this, true);

        mTitleText = (TextView) findViewById(R.id.title);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mValueText = (TextView) findViewById(R.id.value);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, 
                                                          R.styleable.KoraSeekBar, 
                                                          0, 0);
            mSteps = a.getInt(R.styleable.KoraSeekBar_steps, DEFAULT_NSTEPS);
            String title = a.getString(R.styleable.KoraSeekBar_title);
            if(title!=null){
                mTitleText.setText(title);
            } else {
                mTitleText.setVisibility(GONE);
            }
            
            a.recycle();
        } else {
            mSteps = DEFAULT_NSTEPS;
        }
        mSeekBar.setMax(mSteps-1);
    }
    
    public String getTitle()
    {
        return mTitleText.getText().toString();
    }
    
    public void setTitle(String s)
    {
        mTitleText.setText(s);
    }
    
    public boolean isTitleShow()
    {
        return mTitleText.getVisibility() == GONE;
    }
    
    public void setTitleVisible(boolean v)
    {
        if(v)
            mTitleText.setVisibility(VISIBLE);
        else
            mTitleText.setVisibility(GONE);
    }
    
    public String getText()
    {
        return mValueText.getText().toString();
    }
    
    public int getIndex()
    {
        return mSeekBar.getProgress();
    }
    
    public void setIndex(int i)
    {
        mSeekBar.setProgress(i);
    }
    
    public int getNumSteps()
    {
        return mSteps;
    }
    
    public void setNumSteps(int nteps)
    {
        mSeekBar.setMax(mSteps-1);
    }
    
    public boolean isEnabled()
    {
        return mSeekBar.isEnabled();
    }
    
    public void setEnabled(boolean b)
    {
        mSeekBar.setEnabled(b);
        mValueText.setEnabled(b);
    }
}
