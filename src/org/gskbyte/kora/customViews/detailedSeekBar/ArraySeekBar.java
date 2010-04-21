package org.gskbyte.kora.customViews.detailedSeekBar;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ArraySeekBar extends AbstractSeekBar
{
    protected String[] mValues = {"Empty vector"};

    public ArraySeekBar(Context context)
    {
        super(context);
    }
    
    public ArraySeekBar(Context context, String[] values)
    {
        super(context);
        if(values != null && values.length>0){
            mValues = values;
            mSteps = values.length;
            mSeekBar.setMax(mSteps-1);
            mValueText.setText(String.valueOf(mValues[0]) );
        }
        mValueText.setMinWidth(100);
    }

    public ArraySeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, 
                                                          R.styleable.KoraArraySeekBar, 
                                                          0, 0);
            int id = a.getResourceId(R.styleable.KoraArraySeekBar_array_id, R.array.defaultArray);
            
            mValues = context.getResources().getStringArray(id);
            a.recycle();
        }
        
        mSteps = mValues.length;
        mSeekBar.setMax(mSteps-1);
        
        mValueText.setText(String.valueOf(mValues[0]) );
        
        mSeekBar.setOnSeekBarChangeListener(seekListener);
        
        mValueText.setMinWidth(70);
    }
    
    public void setNumSteps(int nteps)
    {
        ;
    }
    
    private OnSeekBarChangeListener seekListener =
        new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser)
            {
                int v = mSeekBar.getProgress();
                mValueText.setText( mValues[v] );
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
