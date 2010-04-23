package org.gskbyte.kora.customViews;

import org.gskbyte.kora.R;

import android.graphics.drawable.PaintDrawable;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class ColorButton extends Button
                         implements ColorDialog.OnClickListener
{
    private int mColor = 0xFF000000;
    private PaintDrawable mPaintDrawable;
    private ColorDialog mDialog = null;
    
    public ColorButton(Context context)
    {
        super(context);
        init();
    }
    
    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    public ColorButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // coger color de atributos!
        init();
    }
    
    protected void init()
    {
        mPaintDrawable = new PaintDrawable();
        mPaintDrawable.setCornerRadius(4);
        mPaintDrawable.setBounds(0, 0, 30, 25);
        setCompoundDrawables(mPaintDrawable, null, null, null);
        
        setText(R.string.colorButtonText);
        
        setOnClickListener(listener);
    }
    
    public int getColor()
    {
        return mColor;
    }
    
    public void setColor(int color)
    {
        mColor = color;
        mPaintDrawable.getPaint().setColor(color);
        invalidate();
    }

    public void setColor(int r, int g, int b)
    {
        setColor( Color.rgb(r, g, b) );
    }
    
    public void setColorHsv(float h, float s, float v)
    {
        setColor( Color.HSVToColor(new float[] {h, s, v}) );
    }
    
    public void setColor(int a, int r, int g, int b)
    {
        setColor( Color.argb(a, r, g, b) );
    }

    public void setColorHsv(int a, float h, float s, float v)
    {
        setColor( Color.HSVToColor(a, new float[] {h, s, v}) );
    }
    
    @Override
    public void onClick(Object tag, int color)
    {
        setColor(color);
    }
    
    private View.OnClickListener listener =
        new OnClickListener(){
            @Override
            public void onClick(View v)
            {                    
                if(mDialog==null)
                    mDialog = new ColorDialog(ColorButton.this.getContext(),
                                              true,
                                              ColorButton. this,
                                              mColor,
                                              ColorButton.this,
                                              0);
                mDialog.show();
            }
        
        };

    
    
}
