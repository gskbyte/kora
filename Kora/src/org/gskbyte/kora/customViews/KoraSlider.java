package org.gskbyte.kora.customViews;

import java.util.Vector;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.MotionEvent;

public class KoraSlider extends KoraView
{
    public static final String TAG = "KoraSlider";
    
    public static class State
    {
        protected String text;
        protected Bitmap icon;
        
        public State(String text, Bitmap icon)
        {
            this.text = text;
            this.icon = icon;
        }
    }
    
    // Propiedades generales del botón
    protected boolean mFocused, mBlocked;
    protected KoraView.Attributes mAttrs;
    
    // Variables de estado del slider
    protected Vector<State> mStates;
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
    protected CountDownTimer mBlockTimer;
    protected static Vibrator sVibrator = null;
    
    protected OnClickListener mClickListener;
    
    protected KoraSlider(Context context)
    {
        super(context);
    }
    
    public KoraSlider(Context context, Vector<State> states, Attributes attr)
    {
        super(context);
        init(states, attr);
    }
    
    /*
    public KoraSlider(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public KoraSlider(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    */
    
    protected void init(Vector<State> states, Attributes attr)
    {
        // Attributes
        mAttrs = attr;
        
        // States
        mStates = states;
        if(mAttrs.caps){
            for(State s : mStates)
                s.text = s.text.toUpperCase();
        }
        
        // Others
        if(sVibrator==null)
            sVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        mBlockTimer = new CountDownTimer(200, 200) {
            public void onTick(long millisLeft) {
            }

            public void onFinish() {
                deselect();
            }
        };

        mFocused = mBlocked = false;
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
            borderColor = mAttrs.borderColors[Attributes.INDEX_NORMAL];
        }
        
        sPaint.setColor(borderColor);
        RectF borderRect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(borderRect, 5, 5, sPaint);
        sPaint.setColor(bgColor);
        RectF backRect  = new RectF(mBorderSize, mBorderSize,
                mWidth-mBorderSize, mHeight-mBorderSize);
        canvas.drawRoundRect(backRect, 6, 6, sPaint);
        
        // Draw icon
        canvas.drawBitmap(mStates.get(mCurrentState).icon, mIconX, mIconY, null);
        
        // Draw text
        if(mAttrs.showText) {
            sPaint.setColor(mAttrs.textColor);
            sPaint.setTextAlign(Align.LEFT);
            sPaint.setTextSize(mTextSize);
            canvas.drawText(mStates.get(mCurrentState).text, mTextX, mTextY, sPaint);
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
        /*
        sPaint.setColor(bgColor);
        RectF sliderMarkBgRect = new RectF(startx+mSliderBorderSize,
                                       mSliderMarkY+mSliderBorderSize, 
                                       startx+mSliderMarkWidth-mSliderBorderSize,
                                       mSliderMarkY+mSliderMarkHeight-mSliderBorderSize);
        canvas.drawRoundRect(sliderMarkBgRect, 5, 5, sPaint);*/
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
        /* Si no hay texto, el icono ocupa toda la parte superior del botón.
         * Se asume que todos los iconos tienen el mismo tamaño (en caso contrario, se deforman)
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
        // Escalar todos los iconos
        for(State s : mStates){
            int iw = s.icon.getWidth(),
                ih = s.icon.getHeight();
            int rw = (maxHeight<<10) / iw,
                rh = (maxHeight<<10) / ih;
            int res = Math.min(rw, rh);
            
            mIconWidth  = (res*iw)>>10;
            mIconHeight = (res*ih)>>10;
            
            mIconX = ((maxWidth-mIconWidth)>>1) + (mBorderSize<<1);
            mIconY = ((maxHeight-mIconHeight)>>1) + (mBorderSize<<1);
            
            s.icon = Bitmap.createScaledBitmap(s.icon, mIconWidth, mIconHeight, true);
        }
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
        
        Rect bounds = new Rect();
        sPaint.setTextSize(mAttrs.textMaxSize);
        mTextSize = mAttrs.textMaxSize;
        for(State s : mStates){
            boolean ok = false;
            while(!ok){
                sPaint.getTextBounds(s.text, 0, s.text.length(), bounds);
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

        int nStates = mStates.size();
        if(nStates>0){
            mSliderMarkX = mSliderBarX;
            mSliderMarkY = mSliderBarY - ((mSliderBarYEnd - mSliderBarY)>>1);
            
            mSliderMarkWidth = (mSliderBarXEnd - mSliderBarX)/nStates;
            mSliderMarkHeight = (mSliderBarYEnd - mSliderBarY)<<1;
        }
    }
    
    public void setOnClickListener(OnClickListener listener)
    {
        mClickListener = listener;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        
        if(!mBlocked){
            int x = (int) event.getX(),
                y = (int) event.getY();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    if(x<mWidth && y<mHeight &&
                       x>0 && y>(mHeight>>1)){
                        mFocused = true;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(x<mSliderBarXEnd && y<mHeight &&
                       x>mSliderBarX && y>0 && mFocused){
                        if(x<mSliderBarX){
                            mCurrentState = 0;
                        }else if(x>mSliderBarXEnd){
                            mCurrentState = mStates.size()-1;
                        }else{
                            //x -= mSliderBarX;
                            int pixelsPerState = (mSliderBarXEnd - mSliderBarX)/(mStates.size()-1);
                            mCurrentState = x / pixelsPerState;
                            invalidate();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(x<mWidth && y<mHeight &&
                       x>0 && y>0 && mFocused){
                        mBlocked = true;
                        
                        mBlockTimer.start();
                        
                        if(mAttrs.vibrate)
                            sVibrator.vibrate(100);
                        
                        if(mClickListener!=null)
                            mClickListener.onClick(this);
                        
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
    
    public void deselect()
    {
        mBlocked = false;
        invalidate();
    }

    public Vector<State> getStates()
    {
        return mStates;
    }
    
    public int getCurrentState()
    {
        return mCurrentState;
    }
    
    public void setState(int index)
    {
        mCurrentState = index;
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

