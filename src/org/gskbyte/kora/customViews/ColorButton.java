package org.gskbyte.kora.customViews;

import org.gskbyte.kora.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class ColorButton extends ImageButton
                         implements ColorDialog.OnClickListener
{
    private Paint mPaint = new Paint();
    private ColorDialog mDialog = null;
    
    public ColorButton(Context context)
    {
        super(context);
        mPaint.setColor(0xFF000000);
        setOnClickListener(listener);
    }
    
    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mPaint.setColor(0xFF000000);
        setOnClickListener(listener);
    }
    
    public ColorButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // coger color
        mPaint.setColor(0xFF000000);
        setOnClickListener(listener);
    }
    
    public int getColor()
    {
        return mPaint.getColor();
    }
    
    public void setColor(int color)
    {
        mPaint.setColor(color);
        invalidate();
    }

    public void setColor(int r, int g, int b)
    {
        mPaint.setColor( Color.rgb(r, g, b) );
    }
    
    public void setColor(int a, int r, int g, int b)
    {
        mPaint.setColor( Color.argb(a, r, g, b) );
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int width = getWidth(),
            height = getHeight();
        int min = (height<width) ? height : width;
        int border = min / 4;
        
        Rect r = new Rect(border, border, width-border-1, height-border-3);
        canvas.drawRect(r, mPaint);
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
                //if(mDialog==null)
                    mDialog = new ColorDialog(ColorButton.this.getContext(),
                                              true,
                                              ColorButton. this,
                                              mPaint.getColor(),
                                              ColorButton.this,
                                              0);
                mDialog.show();
            }
        
        };

    
    
}
