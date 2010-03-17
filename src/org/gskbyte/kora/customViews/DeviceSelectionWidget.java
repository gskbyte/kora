package org.gskbyte.kora.customViews;

import org.gskbyte.kora.device.Device;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.WebIconDatabase.IconListener;

public class DeviceSelectionWidget extends View
{
    private static final String TAG = "DeviceSelectionWidget";
    
    private final static int WIDTH_PADDING = 8;
    private final static int HEIGHT_PADDING = 10;
    private final String label;
    private final int imageResId;
    private final Bitmap image;
    //private final InternalListener listenerAdapter = new InternalListener();
    
    public DeviceSelectionWidget(Context context, int resImage, String label)
    {
        super(context);
        this.label = label;
        this.imageResId = resImage;
        this.image = BitmapFactory.decodeResource(context.getResources(), imageResId);
        
        setFocusable(true);
        setBackgroundColor(Color.WHITE);
        
        //setOnClickListener(listenerAdapter);
        setClickable(true);
    }
    
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        if (gainFocus == true){
            this.setBackgroundColor(Color.rgb(255, 165, 0));
        } else {
            this.setBackgroundColor(Color.WHITE);
        }
    }

    protected void onDraw(Canvas canvas)
    {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        canvas.drawBitmap(image, WIDTH_PADDING / 2, HEIGHT_PADDING / 2, null);
        canvas.drawText(label, WIDTH_PADDING / 2 + image.getWidth()/2, (HEIGHT_PADDING / 2) + image.getHeight() + 8, textPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        @SuppressWarnings("unused")
        int width = MeasureSpec.getSize(widthMeasureSpec),
            height = MeasureSpec.getSize(heightMeasureSpec);

        
        setMeasuredDimension(measureWidth(widthMeasureSpec), 
                             measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec)
    {
        int preferred = image.getWidth() + WIDTH_PADDING;
        return getMeasurement(measureSpec, preferred);
    }
    
    private int measureHeight(int measureSpec)
    {
        int preferred = image.getHeight() + HEIGHT_PADDING + 8;
        return getMeasurement(measureSpec, preferred);
    }
    
    private int getMeasurement(int measureSpec, int preferred)
    {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;
          
        switch(MeasureSpec.getMode(measureSpec))
        {
        case MeasureSpec.EXACTLY:
            // This means the width of this view has been given.
            measurement = specSize;
            break;
        case MeasureSpec.AT_MOST:
            // Take the minimum of the preferred size and what
            // we were told to be.
            measurement = Math.min(preferred, specSize);
            break;
        default: // MeasureSpec.UNSPECIFIED
            measurement = preferred;
            break;
        }
        return measurement;
    }

  /*  public void setOnClickListener(OnClickListener newListener)
    {
        listenerAdapter.setListener(newListener);
    }
*/
    public String getLabel()
    {
        return label;
    }
    
    public int getImageResId()
    {
        return imageResId;
    }

    private class InternalListener implements View.OnClickListener
    {

        private OnClickListener listener = null;
   
        public void setListener(OnClickListener newListener)
        {
            listener = newListener;
        }
       
        @Override
        public void onClick(View v) 
        {
            if (listener != null)
            {
                 listener.onClick(DeviceSelectionWidget.this);
            }
        }
    }

    
}
