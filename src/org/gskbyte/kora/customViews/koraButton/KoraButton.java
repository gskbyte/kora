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
import android.graphics.Typeface;
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

    public static final int STATE_NORMAL = 0, STATE_FOCUSED = 1,
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
        String t = mText;
        Bitmap b = Bitmap.createScaledBitmap(mIcon, mIconWidth, mIconHeight,
                true);
        canvas.drawBitmap(b, mIconX, mIconY, null);

        // Texto
        if (mAttributes.showText) {
            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.BLACK);
            textPaint.setTypeface(mAttributes.typeface);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(mAttributes.textScale*(mWidth>>3));
            canvas.drawText(mText, mTextX, mTextY, textPaint);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        calculateCoordinates();

        setMeasuredDimension(mWidth, mHeight);
    }

    protected void calculateCoordinates()
    {
        int iconX=0, iconY=0, iconW=0, iconH=0;
        
        // El ancho del borde se toma relativo a la anchura del widget
        if (mAttributes.showBorder) {
            mBackX = mBackY = mWidth >> 4; // width/16
            mBackXMax = mWidth - mBackX;
            mBackYMax = mHeight - mBackX;
        } else {
            mBackX = mBackY = 0; // width/16
            mBackXMax = mWidth;
            mBackYMax = mHeight;
        }

        if (mAttributes.orientation == Attributes.HORIZONTAL) {
            
        } else {
            iconX = iconY = (mWidth >> 4) + mBackX; // width*2/16
            iconW = mWidth - (iconX << 1);
            if (mAttributes.showText) {
                iconH = mHeight - (iconY << 2);
                mTextX = mWidth * 8 / 16;
                mTextY = mHeight * 14 / 16;
            } else {
                iconH = mHeight - (iconY << 1);
            }
        }

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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

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
        public int[] borderColors = { Color.GREEN, Color.WHITE, Color.WHITE };

        public int[] backgroundColors = { Color.WHITE, Color.WHITE, Color.WHITE };

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
}
