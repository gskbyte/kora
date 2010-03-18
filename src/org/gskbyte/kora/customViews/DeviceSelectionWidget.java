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
    private final InternalListener listenerAdapter = new InternalListener();
    
    private int mWidth, mHeight;
    private int iconX, iconY, iconWidth, iconHeight;
   
    
    public DeviceSelectionWidget(Context context, int resImage, String label)
    {
        super(context);
        this.label = label;
        this.imageResId = resImage;
        this.image = BitmapFactory.decodeResource(context.getResources(), imageResId);
        
        setFocusable(true);
        //setBackgroundColor(Color.WHITE);
        
        //setOnClickListener(listenerAdapter);
        setClickable(true);
        this.setPadding(10, 10, 10, 10);
    }
    
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        /*
        if (gainFocus == true){
            this.setBackgroundColor(Color.rgb(255, 165, 0));
        } else {
            this.setBackgroundColor(Color.WHITE);
        }
        */
    }

    protected void onDraw(Canvas canvas)
    {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        Bitmap b = Bitmap.createScaledBitmap(image, iconWidth, iconHeight, true);
        canvas.drawBitmap(b, iconX, iconY, null);
        canvas.drawText(label, iconX+b.getWidth()/2, iconY+b.getHeight() + 8, textPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Calcular tamaños de las cosas aquí
        iconX = mWidth / 10;
        iconY = mHeight / 10;
        iconWidth = 7*mWidth/10;
        iconHeight = 7*mHeight/10;
        
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setOnClickListener(OnClickListener newListener)
    {
        listenerAdapter.setListener(newListener);
    }

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
