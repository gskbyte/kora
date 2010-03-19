package org.gskbyte.kora.customViews.koraButton;

import org.gskbyte.kora.device.Device;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.WebIconDatabase.IconListener;
import android.widget.Toast;

public class KoraButton extends View
{
    private static final String TAG = "KoraButton";
    
    // Propiedades generales del botón
    private int mWidth, mHeight;
    
    // Borde
    private boolean drawBorder;
    private int borderColor;
    
    // Fondo
    private int backX, backY, backXMax, backYMax;
    private int backColor;
    
    // Icono
    private int iconX, iconY, iconWidth, iconHeight;
    private Bitmap image;
    private int imageResId;
    
    // Texto
    private  String label;
    private int textX, textY;
    
    public KoraButton(Context context, int resImage, String label)
    {
        super(context);
        this.label = label;
        this.imageResId = resImage;
        this.image = BitmapFactory.decodeResource(context.getResources(), imageResId);
        
        setFocusable(true);
        setClickable(true);
        
        borderColor = Color.rgb(255, 165, 0);
        backColor = Color.WHITE;
        
        this.setPadding(10, 10, 20, 10);
    }
    
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
    	if (gainFocus == true){
            borderColor = Color.CYAN;
        } else {
        	borderColor = Color.rgb(255, 165, 0);
        }
    	
    	invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
    	// Pintar borde
    	Paint borderPaint = new Paint();
    	borderPaint.setColor(borderColor);
    	RectF borderRect = new RectF(0, 0, mWidth, mHeight);
    	canvas.drawRoundRect(borderRect, 10, 10, borderPaint);
    	
    	// Pintar fondo
    	final Paint backPaint = new Paint();
        backPaint.setAntiAlias(true); 
    	backPaint.setColor(backColor);
    	RectF backRect = new RectF(backX, backY, backXMax, backYMax);
    	canvas.drawRoundRect(backRect, 10, 10, backPaint);
    	
    	// Icono
        Bitmap b = Bitmap.createScaledBitmap(image, iconWidth, iconHeight, true);
        canvas.drawBitmap(b, iconX, iconY, null);

        // Texto
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, textX, textY, textPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Calcular tamaños de las cosas aquí
        backX = mWidth / 16;
        backY = mHeight / 16;
        backXMax = mWidth * 15/16;
        backYMax = mHeight* 15/16;
        
        iconX = mWidth * 2/16;
        iconY = mHeight * 2/16;
        iconWidth = mWidth * 12/16;
        iconHeight = mHeight * 10/16;
        
        // ESCALAR IMAGEN
        
        textX = mWidth * 8/16;
        textY =  mHeight * 14/16;
        
        setMeasuredDimension(mWidth, mHeight);
    }

    public String getLabel()
    {
        return label;
    }
    
    public int getImageResId()
    {
        return imageResId;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
            	
            }

            return true;
    }
}
