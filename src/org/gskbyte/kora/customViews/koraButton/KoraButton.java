package org.gskbyte.kora.customViews.koraButton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

public class KoraButton extends View
{
    private static final String TAG = "KoraButton";

    public static final int STATE_NORMAL = 0,
                            STATE_FOCUSED = 1,
                            STATE_SELECTED = 2;

    public KoraButton(Context context, String text, Drawable icon,
            Attributes attr)
    {
        super(context);
        // this(context, text, icon.d)
    }

    public KoraButton(Context context, String text, int iconId, Attributes attr)
    {
        this(context, text, BitmapFactory.decodeResource(
                context.getResources(), iconId), attr);
    }

    public KoraButton(Context context, String text, Bitmap icon, Attributes attr)
    {
        super(context);

        if (text != null)
            mText = text;
        else
            mText = text;

        mIcon = icon;

        if (attr != null)
            mAttributes = attr;
        else
            mAttributes = new Attributes();

        mState = STATE_NORMAL;

        setFocusable(true);
        setClickable(true);
    }

    protected void onFocusChanged(boolean gainFocus, int direction,
            Rect previouslyFocusedRect)
    {
        if (gainFocus) {
            mState = STATE_FOCUSED;
        } else {
            mState = STATE_NORMAL;
        }

        invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
        // Pintar borde
        if (mAttributes.showBorder) {
            Paint borderPaint = new Paint();
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(mAttributes.borderColors[mState]);
            RectF borderRect = new RectF(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(borderRect, 10, 10, borderPaint);
        }

        // Pintar fondo
        final Paint backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(mAttributes.backgroundColors[mState]);
        RectF backRect;
        if (mAttributes.showBorder)
            backRect = new RectF(mBackX, mBackY, mBackXMax, mBackYMax);
        else
            backRect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(backRect, 6, 6, backPaint);

        // Icono
        Bitmap b = Bitmap.createScaledBitmap(mIcon, mIconWidth, mIconHeight,
                true);
        canvas.drawBitmap(b, mIconX, mIconY, null);

        // Texto
        if (mAttributes.showText) {
            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.BLACK);
            textPaint.setTypeface(mAttributes.typeface);
            textPaint.setTextSize(80);
            
            int maxWidth = mWidth / 2;
            
            boolean ok = false;
            Rect    bounds = new Rect();
            float size = 80;
            
            if(mAttributes.orientation == Attributes.VERTICAL){
                textPaint.setTextAlign(Paint.Align.CENTER);
                maxWidth = mWidth;
            }
            while(!ok){
                textPaint.getTextBounds(mText, 0, mText.length(), bounds);
                if(bounds.width()<maxWidth){
                    ok = true;
                } else {
                    size -= 0.25;
                    textPaint.setTextSize(size);
                }
            }
            
            canvas.drawText(mText, mTextX, mTextY, textPaint);
            /*
            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.BLACK);
            textPaint.setTypeface(mAttributes.typeface);
            if(mAttributes.orientation == Attributes.VERTICAL){
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(mAttributes.textScale*(Math.min(mWidth, mHeight)>>2));
            } else {
                textPaint.setTextSize(mAttributes.textScale*Math.min(mWidth, mHeight)/3);
            }
            canvas.drawText(mText, mTextX, mTextY, textPaint);
            */
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        if(mWidth>2.25*mHeight)
            mAttributes.orientation = Attributes.HORIZONTAL;
        else
            mAttributes.orientation = Attributes.VERTICAL;

        calculateCoordinates();

        setMeasuredDimension(mWidth, mHeight);
    }

    protected void calculateCoordinates()
    {
        int iconX=0, iconY=0, iconW=0, iconH=0;
        int minSize = Math.min(mWidth, mHeight);
        
        // El ancho del borde se toma relativo a la anchura del widget
        if (mAttributes.showBorder) {
            mBackX = mBackY = (int)((minSize >> 4)*mAttributes.borderScale);
            mBackXMax = mWidth - mBackX;
            mBackYMax = mHeight - mBackX;
        } else {
            mBackX = mBackY = 0; // width/16
            mBackXMax = mWidth;
            mBackYMax = mHeight;
        }
        
        iconX = iconY = (minSize >> 4) + mBackX; // width*2/16
        if (mAttributes.orientation == Attributes.HORIZONTAL) {
            iconH = mHeight - (iconY << 1);
            if (mAttributes.showText) {
                iconW = (mWidth>>1) - (iconX << 1);
                mTextX = mWidth>>1;
                mTextY = (mHeight>>1) + (int)mAttributes.textScale*8;
            } else {
                iconW = mWidth - (iconX << 1);
            }
        } else {
            iconW = mWidth - (iconX << 1);
            if (mAttributes.showText) {
                iconH = mHeight - (iconY << 1) - (int)(mAttributes.textScale*(minSize>>2));
                mTextX = mWidth >>1;
                mTextY = mHeight * 14 / 16;
            } else {
                iconH = mHeight - (iconY << 1);
            }
        }
        
        // Establecer tamaño mínimo para iconos
        if(iconW < 10)
            iconW = 10;

        if(iconH < 10)
            iconH = 10;

        // Escalar icono
        // para las proporciones, multplico y divido por 1024
        // en vez de usar flotantes, es algo más rápido
        int w = mIcon.getWidth(),
            h = mIcon.getHeight();
        int rw = (iconW<<10) / w,
            rh = (iconH<<10) / h;
        
        int res = (rw < rh) ? rw : rh;
        
        mIconWidth = (res*w)>>10;
        mIconHeight = (res*h)>>10;
        mIconX = iconX + (Math.abs(mIconWidth - iconW)>>1);
        mIconY = iconY + (Math.abs(mIconHeight - iconH)>>1);
    }
    
    public void setOnClickListener(OnClickListener newListener)
    {
        clickListener = newListener;
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        int action = event.getAction();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                this.mState = STATE_FOCUSED;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                @SuppressWarnings("unused")
                int x = (int) event.getX(),
                    y = (int) event.getY();
                
                if(x<mWidth && y<mHeight && mState==STATE_FOCUSED){
                    mState = STATE_SELECTED;
                    if(clickListener!=null)
                        clickListener.onClick(this);
                } else {
                    mState = STATE_NORMAL;
                }
                invalidate();
                break;
        }

        return true;
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String text)
    {
        mText = text;
        invalidate();
    }

    public Bitmap getIcon()
    {
        return mIcon;
    }

    public void setIcon(Bitmap icon)
    {
        mIcon = icon;
        invalidate();
    }

    public Attributes getAttributes()
    {
        return mAttributes;
    }

    public void setAttributes(Attributes attr)
    {
        mAttributes = attr;
        calculateCoordinates();
        invalidate();
    }

    public class Attributes
    {
        public static final int HORIZONTAL = 0, VERTICAL = 1;

        public int orientation = VERTICAL;

        public boolean showText = true;
        public float textScale = (float) 1.0;
        public Typeface typeface = Typeface.DEFAULT;

        public boolean showBorder = true;
        public float borderScale = (float) 1.0;
        public int[] borderColors = { Color.rgb(52,139,212), Color.rgb(255, 165, 0), Color.rgb(255, 165, 0) };

        public int[] backgroundColors = { Color.WHITE, Color.WHITE, Color.rgb(255, 165, 0) };

        public Attributes()
        {
        }

        public Attributes(int orientation, boolean showText,
                boolean showBorder, int[] borderColors, int[] backgroundColors,
                Typeface typeface)
        {
            this.orientation = orientation;
            this.showText = showText;
            this.showBorder = showBorder;

            if (borderColors.length == 3)
                this.borderColors = borderColors;
            if (backgroundColors.length == 3)
                this.backgroundColors = backgroundColors;
        }
    }
    
    // Propiedades generales del botón
    private String mText;
    private Bitmap mIcon;
    private int mState;
    private Attributes mAttributes;

    // Variables utilizadas para dibujar
    private int mWidth, mHeight;
    private int mBackX, mBackY, mBackXMax, mBackYMax;
    private int mIconX, mIconY, mIconWidth, mIconHeight;
    private int mTextX, mTextY;
    
    private OnClickListener clickListener;
}
