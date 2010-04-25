package org.gskbyte.kora.customViews.koraSlider;

import org.gskbyte.kora.customViews.KoraView;
import org.gskbyte.kora.customViews.KoraView.Attributes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnClickListener;

public class KoraSlider extends KoraView
{
    public static final String TAG = "KoraSlider";
    
    // Propiedades generales del botón
    protected String mText = "";
    protected Bitmap mIcon;
    protected boolean mFocused, mBlocked;
    protected KoraView.Attributes mAttrs;
    
    // Variables de estado del slider
    protected int nStates;
    protected int mCurrentState;
    
    // Variables de dibujo
    protected int mWidth, mHeight;
    protected int mBorderSize;
    protected int mIconX, mIconY, mIconWidth, mIconHeight;
    protected int mTextX, mTextY;
    protected float mTextSize;
    
    protected int mSliderBarX, mSliderBarY, mSliderBarXEnd, mSliderBarYEnd;
    protected int mSliderBorderSize;
    protected int mSliderMarkX, mSliderMarkWidth, mSliderMarkY, mSliderMarkHeight;
    
    // Variables para realimentación
    protected CountDownTimer mSelectionTimer;
    protected static Vibrator sVibrator = null;
    
    protected OnClickListener mClickListener;
    
    protected KoraSlider(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public KoraSlider(Context context, String text, int iconId)
    {
        this(context, text, BitmapFactory.decodeResource(
                context.getResources(), iconId));
    }
    
    public KoraSlider(Context context, String text, int iconId, Attributes attr)
    {
        this(context, text, BitmapFactory.decodeResource(
                context.getResources(), iconId), attr);
    }

    public KoraSlider(Context context, String text, Bitmap icon)
    {
        this(context, text, icon, null);
    }
    
    public KoraSlider(Context context, String text, Bitmap icon, Attributes attr)
    {
        super(context);
        
        init(text, icon, attr);
    }
    
    public KoraSlider(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public KoraSlider(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /* 
         * NOT YET IMPLEMENTED 
         */
    }
    
    protected void init(String text, Bitmap icon, Attributes attr)
    {
        mText = (text == null) ? "" : text;
        mIcon = icon;
        mFocused = mBlocked = false;
        
        if(attr!=null)
            mAttrs = attr;
        else
            mAttrs = new Attributes();
        
        if(sVibrator==null)
            sVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        mSelectionTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisLeft) {
            }

            public void onFinish() {
                deselect();
            }
        };

        setFocusable(true);
        setClickable(true);
    }
    
    protected void onFocusChanged(boolean gainFocus, int direction,
            Rect previouslyFocusedRect)
    {
        if (gainFocus) {
            mFocused = true;
        } else {
            mFocused = false;
        }

        invalidate();
    }
    
    protected void onDraw(Canvas canvas)
    {
        sPaint.setAntiAlias(true);
        
        // Draw border and background
        int bgColor, borderColor;
        if(mFocused) {
            bgColor = mAttrs.backgroundColors[Attributes.INDEX_FOCUSED];
            borderColor = mAttrs.borderColors[Attributes.INDEX_FOCUSED];
        } else if (mBlocked) {
            bgColor = mAttrs.backgroundColors[Attributes.INDEX_SELECTED];
            borderColor = mAttrs.borderColors[Attributes.INDEX_SELECTED];
        } else {
            bgColor = mAttrs.backgroundColors[Attributes.INDEX_NORMAL];
            borderColor = mAttrs.borderColors[Attributes.INDEX_FOCUSED];
        }
        
        sPaint.setColor(borderColor);
        RectF borderRect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(borderRect, 5, 5, sPaint);
        sPaint.setColor(bgColor);
        RectF backRect  = new RectF(mBorderSize, mBorderSize,
                mWidth-mBorderSize, mHeight-mBorderSize);
        canvas.drawRoundRect(backRect, 6, 6, sPaint);
        
        // Draw icon
        /// OPTIMIZAR ESTO!!!
        Bitmap b = Bitmap.createScaledBitmap(mIcon, mIconWidth, mIconHeight,
                true);
        canvas.drawBitmap(b, mIconX, mIconY, null);
        
        // Draw text
        if(mAttrs.showText) {
            sPaint.setColor(mAttrs.textColor);
            sPaint.setTextAlign(Align.LEFT);
            sPaint.setTextSize(mTextSize);
            canvas.drawText(mText, mTextX, mTextY, sPaint);
        }
        
        // Draw slider (normal bg always, border changes)
        bgColor = mAttrs.backgroundColors[Attributes.INDEX_NORMAL];
        if (mBlocked) {
            borderColor = mAttrs.borderColors[Attributes.INDEX_SELECTED];
        } else {
            borderColor = mAttrs.borderColors[Attributes.INDEX_FOCUSED];
        }
        
        sPaint.setColor(borderColor);
        RectF sliderBorderRect = new RectF(mSliderBarX, mSliderBarY, 
                                           mSliderBarXEnd, mSliderBarYEnd);
        canvas.drawRoundRect(sliderBorderRect, 5, 5, sPaint);
        
        sPaint.setColor(bgColor);
        RectF sliderBgRect = new RectF(mSliderBarX+mSliderBorderSize,
                                       mSliderBarY+mSliderBorderSize, 
                                       mSliderBarXEnd-mSliderBorderSize,
                                       mSliderBarYEnd-mSliderBorderSize);
        canvas.drawRoundRect(sliderBgRect, 5, 5, sPaint);
        
        // Draw slider mark
        sPaint.setColor(borderColor);
        int startx = mSliderMarkX+mCurrentState*mSliderMarkWidth;
        RectF sliderMarkBorderRect = new RectF(startx, mSliderMarkY, 
                startx+mSliderMarkWidth, mSliderMarkY+mSliderMarkHeight);
        canvas.drawRoundRect(sliderMarkBorderRect, 5, 5, sPaint);
        
        sPaint.setColor(bgColor);
        RectF sliderMarkBgRect = new RectF(startx+mSliderBorderSize,
                                       mSliderMarkY+mSliderBorderSize, 
                                       startx+mSliderMarkWidth-mSliderBorderSize,
                                       mSliderMarkY+mSliderMarkHeight-mSliderBorderSize);
        canvas.drawRoundRect(sliderMarkBgRect, 5, 5, sPaint);
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        if(mWidth >0 && mHeight>0){
            calculateBorder();
            calculateIconBounds();
            if(mAttrs.showText) {
                calculateTextBounds();
            }
            calculateSliderBounds();
        }
        setMeasuredDimension(mWidth, mHeight);
    }
    
    protected void calculateBorder()
    {
        // 1/16 del mínimo lado
        mBorderSize = Math.min(mWidth, mHeight)>>4;
    }
    
    protected void calculateIconBounds()
    {
        /* Si no hay texto, el icono ocupa toda la parte superior del botón
         * 
         * Si hay, el icono ocupa 1/2 del alto tanto en vertical como en horizontal
         * 
         * No se permiten valores menores o iguales que 0.
         */
        
        int maxWidth, maxHeight;
        
        maxHeight = (mHeight>>1) - (mBorderSize<<1);
        if(mAttrs.showText) {
            maxWidth = maxHeight;
        } else {
            maxWidth = mWidth - (mBorderSize<<1);
        }
        
        // Multiplico y divido por 1024 para hallar las proporciones, es más rápido
        // que usar floats
        int iw = mIcon.getWidth(),
            ih = mIcon.getHeight();
        int rw = (maxHeight<<10) / iw,
            rh = (maxHeight<<10) / ih;
        int res = Math.min(rw, rh);
        
        mIconWidth  = (res*iw)>>10;
        mIconHeight = (res*ih)>>10;
        
        mIconX = ((maxWidth-mIconWidth)>>1) + (mBorderSize<<1);
        mIconY = ((maxHeight-mIconHeight)>>1) + (mBorderSize<<1);
    }
    
    protected void calculateTextBounds()
    {
        /*
         * El texto ocupa un máximo de:
         * - Ancho del widget- alto/2
         * - La mitad superior del alto
         */
        sPaint.setAntiAlias(false);
        sPaint.setTypeface(mAttrs.typeface);
        
        int maxWidth, maxHeight;
        
        maxWidth = mWidth - (mHeight>>1) - (mBorderSize<<3);
        maxHeight = (mHeight>>1) - (mBorderSize<<1);
        
        boolean ok = false;
        Rect bounds = new Rect();
        sPaint.setTextSize(mAttrs.textMaxSize);
        mTextSize = mAttrs.textMaxSize;
        while(!ok){
            sPaint.getTextBounds(mText, 0, mText.length(), bounds);
            if(bounds.width()<=maxWidth &&
               (bounds.height()-bounds.bottom)<=maxHeight){
                ok = true;
                if((mTextSize < sMaxTextSize || sMaxTextSize == -1) && !mAttrs.overrideMaxSize)
                    sMaxTextSize = mTextSize;
            } else {
                mTextSize -= 1.0f;
                sPaint.setTextSize(mTextSize);
            }
        }
        
        mTextX = (mHeight>>1) + (mBorderSize);
        mTextY = (((mHeight>>1)+bounds.height())>>1) +(mBorderSize<<1);
    }
    
    protected void calculateSliderBounds()
    {
        // El slider ocupa toda la mitad inferior del componente
        mSliderBarX = mBorderSize<<1;
        mSliderBarY = (mHeight>>1)+(mBorderSize*3);
        
        mSliderBarXEnd = mWidth-(mBorderSize<<1);
        mSliderBarYEnd = mHeight-(mBorderSize*3);
        
        mSliderBorderSize = (mSliderBarYEnd -mSliderBarY)/6;

        // Posiciones iniciales de la marca
        // La marca mide el doble de alto que la barra
        // Y ancho / nposiciones
        mSliderMarkX = mSliderBarX;
        mSliderMarkY = mSliderBarY - ((mSliderBarYEnd - mSliderBarY)>>1);
        nStates=5;
        mSliderMarkWidth = (mSliderBarXEnd - mSliderBarX)/nStates;
        mSliderMarkHeight = (mSliderBarYEnd - mSliderBarY)<<1;
        
        //mSliderMarkX, mSliderMarkWidth, mSliderMarkY, mSliderMarkHeight;
        
    }
    
    public void setOnClickListener(OnClickListener listener)
    {
        mClickListener = listener;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        return true;
    }
    
    public void deselect()
    {
        mBlocked = false;
        invalidate();
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
    
    public void setIcon(int resourceId)
    {
        mIcon = BitmapFactory.decodeResource(
                getContext().getResources(), resourceId);
        invalidate();
    }

    public Attributes getAttributes()
    {
        return mAttrs;
    }

    public void setAttributes(Attributes attr)
    {
        mAttrs = attr;
        invalidate();
    }
    
}   

