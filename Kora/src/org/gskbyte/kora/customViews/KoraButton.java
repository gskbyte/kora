package org.gskbyte.kora.customViews;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class KoraButton extends KoraView
{
    public static final String TAG = "KoraButton";

    // Propiedades generales del botón
    protected String mText = "";
    protected Bitmap mIcon;
    protected boolean mFocused, mBlocked;
    protected KoraView.Attributes mAttrs;
    protected int mOrientation;

    // Variables utilizadas para dibujar
    protected int mWidth, mHeight;
    protected int mBorderSize;
    protected int mIconX, mIconY, mIconWidth, mIconHeight;
    protected int mTextX, mTextY;
    protected float mTextSize;
    
    // Variables para realimentación
    protected final CountDownTimer mBlockTimer = new CountDownTimer(1000, 1000) {
        public void onTick(long millisLeft) {
        }

        public void onFinish() {
            deselect();
        }
    };
    
    protected static Vibrator sVibrator = null;
    
    protected KoraButton(Context context)
    {
    	super(context);
        init("", null, new Attributes());
    }
    
    public KoraButton(Context context, String text, int iconId, Attributes attr)
    {
        super(context);
        init(text, BitmapFactory.decodeResource(context.getResources(), iconId),
             attr);
    }
    
    public KoraButton(Context context, String text, Bitmap icon, Attributes attr)
    {
        super(context);
        init(text, icon, attr);
    }
    
    public KoraButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public KoraButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        Attributes attr = new Attributes();
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KoraButton, defStyle, 0);
        
        attr.showText = a.getBoolean(R.styleable.KoraButton_showText, false);
        String text = a.getString(R.styleable.KoraButton_text);
        attr.textColor = a.getColor(R.styleable.KoraButton_textColor, Color.BLACK);
        int iconRes = a.getResourceId(R.styleable.KoraButton_icon, 0);
        attr.textMaxSize = a.getFloat(R.styleable.KoraButton_textSize, Attributes.TEXT_MEDIUM);
        attr.overrideMaxSize = a.getBoolean(R.styleable.KoraButton_overrideCommonTextSize, false);
        
        Bitmap icon;
        if(iconRes != 0)
            icon = BitmapFactory.decodeResource(
                    context.getResources(), iconRes);
        else
            icon = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.icon_empty);
        
        init(text, icon, attr);
        
        a.recycle();
    }
    
    protected void init(String text, Bitmap icon, Attributes attr)
    {
        // Attributes
        mAttrs = attr;
        
        // Text
        mText = (mAttrs.caps) ? text.toUpperCase() : text;
        
        // Icon
        mIcon = icon;
        
        // Others
        if(sVibrator==null)
            sVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        mFocused = mBlocked = false;
        setFocusable(true);
        setClickable(true);
    }
    
    public static void resetMinTextSize()
    {
        sMaxTextSize = -1;
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
        
        // Borde
        if(mFocused)
            sPaint.setColor(mAttrs.borderColors[Attributes.INDEX_FOCUSED]);
        else if (mBlocked)
            sPaint.setColor(mAttrs.borderColors[Attributes.INDEX_SELECTED]);
        else
            sPaint.setColor(mAttrs.borderColors[Attributes.INDEX_NORMAL]);
        
        RectF borderRect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(borderRect, 5, 5, sPaint);
        
        // Fondo
        if(mFocused)
        sPaint.setColor(mAttrs.backgroundColors[Attributes.INDEX_FOCUSED]);
        else if (mBlocked)
            sPaint.setColor(mAttrs.backgroundColors[Attributes.INDEX_SELECTED]);
        else
            sPaint.setColor(mAttrs.backgroundColors[Attributes.INDEX_NORMAL]);
        
        RectF backRect  = new RectF(mBorderSize, mBorderSize,
                             mWidth-mBorderSize, mHeight-mBorderSize);
        canvas.drawRoundRect(backRect, 6, 6, sPaint);

        // Icono
        Bitmap b = Bitmap.createScaledBitmap(mIcon, mIconWidth, mIconHeight,
                                             true);
        canvas.drawBitmap(b, mIconX, mIconY, null);

        // Texto
        if(mAttrs.showText) {
            sPaint.setColor(mAttrs.textColor);
            if(mAttrs.overrideMaxSize)
            	sPaint.setTextSize(mTextSize);
            else
            	sPaint.setTextSize(sMaxTextSize);
            	
            if(mOrientation == Attributes.VERTICAL) {
                sPaint.setTextAlign(Align.CENTER);
            } else {
                sPaint.setTextAlign(Align.LEFT);
            }
            canvas.drawText(mText, mTextX, mTextY, sPaint);
        }
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
           
        switch(mAttrs.allowedOrientations){
        case Attributes.VERTICAL:
            mOrientation = Attributes.VERTICAL;
            break;
        case Attributes.HORIZONTAL:
            mOrientation = Attributes.HORIZONTAL;
            break;
        default:
        case Attributes.BOTH:
            if(mWidth>mAttrs.maxProportion*mHeight)
                mOrientation = Attributes.HORIZONTAL;
            else
                mOrientation = Attributes.VERTICAL;
            break;
        }

        calculateBorder();
        calculateIconBounds();
        if(mAttrs.showText) {
            calculateTextBounds();
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
        /* Si no hay texto, el icono ocupa todo el botón
         * 
         * Si hay:
         * 
         * - Si la orientación es horizontal el icono ocupa 1/4 del total
         * - Si es vertical, 3/4.
         * 
         * En cualquier caso se resta el doble de los bordes, o se escala al menor 
         * lado si es menor. La imagen se escala manteniendo proporciones.
         * 
         * No se permiten valores menores o iguales que 0.
         */
        int maxSize,
            maxWidth,
            maxHeight;
        int border = mBorderSize+2,
            doubleBorder = (mBorderSize+2)<<1;
        
        if(mAttrs.showText) {
            if(mOrientation == Attributes.VERTICAL) {
                maxWidth = mWidth - doubleBorder;
                maxHeight = ((mHeight-doubleBorder)*3)/4 ;
            } else {
                maxWidth = (mWidth-doubleBorder)/4;
                maxHeight = mHeight - doubleBorder;
            }
        } else {
            maxWidth = mWidth - doubleBorder;
            maxHeight = mHeight - doubleBorder;
        }
        maxSize = Math.min( maxWidth,
                            maxHeight );
        
        if(maxSize < 1)
            maxSize = 1;
        // Multiplico y divido por 1024 para hallar las proporciones, es más rápido
        // que usar floats
        int iw = mIcon.getWidth(),
            ih = mIcon.getHeight();
        int rw = (maxSize<<10) / iw,
            rh = (maxSize<<10) / ih;
        int res = Math.min(rw, rh);
        mIconWidth = (res*iw)>>10;
        mIconHeight = (res*ih)>>10;
        
        mIconX = border + ((maxWidth-mIconWidth)>>1);
        mIconY = border + ((maxHeight-mIconHeight)>>1);
    }
    
    protected void calculateTextBounds()
    {
        sPaint.setAntiAlias(false);
        sPaint.setTypeface(mAttrs.typeface);
        
        int maxWidth, maxHeight;
        int border = mBorderSize*2;
        
        if(mOrientation == Attributes.VERTICAL) {
            maxWidth = mWidth - border;
            maxHeight = (mHeight-border)/4;
            mTextSize = mAttrs.textMaxSize;
        } else {
            maxWidth = ((mWidth-border)*3)/4;
            maxHeight = mHeight - border;
            mTextSize = mAttrs.textMaxSize*1.5f;
        }
        
        boolean ok = false;
        Rect bounds = new Rect();
        if(!mAttrs.overrideMaxSize && sMaxTextSize>0){
            mTextSize = Math.min(mAttrs.textMaxSize, sMaxTextSize);
        } else {
            mTextSize = mAttrs.textMaxSize;
        }
        sPaint.setTextSize(mTextSize);
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
        
        if(mOrientation == Attributes.VERTICAL) {
            mTextX = mWidth>>1;
            mTextY = mHeight - maxHeight/2;
        } else {
            mTextX = mWidth/4 + mBorderSize;
            mTextY = (mHeight+bounds.height())>>1;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        if(!mBlocked){ // aceptar eventos cuando no estoy bloqueado
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    mFocused = true;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    int x = (int) event.getX(),
                        y = (int) event.getY();
                    
                    if(x<mWidth && y<mHeight &&
                       x>0 && y>0 && mFocused){
                        mBlocked = true;
                        
                        mBlockTimer.start();
                        
                        if(mAttrs.vibrate)
    	                    sVibrator.vibrate(500);
                        
                        performClick();
                        
                    } else {
                        mBlocked = false;
                    }
                    mFocused = false;
                    
                    invalidate();
                    break;
            }
        }

        return true;
    }
    

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        super.onKeyDown(keyCode, event);
        
        if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mBlocked = true;
            
            mBlockTimer.start();
            
            if(mAttrs.vibrate)
                sVibrator.vibrate(500);
            
            performClick();
            return true;
        }
        
        return false;
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
        init(mText, mIcon, attr);
        invalidate();
    }
}
