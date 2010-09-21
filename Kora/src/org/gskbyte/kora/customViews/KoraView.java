package org.gskbyte.kora.customViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class KoraView extends View
{
	public static class Attributes
    {
        public static final int HORIZONTAL = 1,
                                VERTICAL = 2,
                                BOTH = 3; // HORIZONTAL | VERTICAL
        
        public static final float TEXT_LARGE = 60,
                                  TEXT_MEDIUM = 75,
                                  TEXT_SMALL = 25;


        public static final int INDEX_NORMAL = 0,
                                INDEX_FOCUSED = 1,
                                INDEX_SELECTED = 2;
        
        public static final int BG_NORMAL_COLOR = Color.WHITE,
                                BG_FOCUSED_COLOR = Color.WHITE,
                                BG_SELECTED_COLOR = 0xFF7FB6E8; // 127, 182, 232 rgb
        
        public static final int BORDER_NORMAL_COLOR = 0xFF348BD4, // 52 139 212 rgb
                                BORDER_FOCUSED_COLOR = 0xFF7FB6E8,
                                BORDER_SELECTED_COLOR = 0xFF7FB6E8;
        
        // Orientación
        public int allowedOrientations = BOTH;
        public float maxProportion = 2.25f; // máxima relación v/h para poner horizontal
        
        // Texto
        public boolean showText = true;
        public float textMaxSize = TEXT_MEDIUM;
        public boolean overrideMaxSize = false;
        public Typeface typeface = Typeface.DEFAULT;
        public int textColor = Color.BLACK;
        public boolean caps = false;
        
        // Fondo
        public int[] backgroundColors = { BG_NORMAL_COLOR,
                                          BG_FOCUSED_COLOR,
                                          BG_SELECTED_COLOR };
        
        // Borde
        public int[] borderColors ={ BG_NORMAL_COLOR,
                                     BORDER_FOCUSED_COLOR,
                                     BORDER_SELECTED_COLOR };
        
        // Vibrar sí/no
        public boolean vibrate = false;
        
        public Attributes()
        {
            
        }
        
        public Attributes(Attributes o)
        {
            allowedOrientations = o.allowedOrientations;
            maxProportion = o.maxProportion;
            showText = o.showText;
            textMaxSize = o.textMaxSize;
            overrideMaxSize = o.overrideMaxSize;
            typeface = o.typeface;
            textColor = o.textColor;
            caps = o.caps;
            
            for(int i=0; i<3; ++i){
                backgroundColors[i] = o.backgroundColors[i];
                borderColors[i] = o.borderColors[i];
            }
            vibrate = o.vibrate;
        }
    }
	
    // Superficie para dibujar
    protected static Paint sPaint = new Paint();
    protected static float sMaxTextSize = -1;
    
	protected KoraView(Context context)
    {
        super(context);
    }
	
	public KoraView(Context context, AttributeSet attrs, int defStyle)
    {
	    super(context, attrs, defStyle);
    }
	
	public static void resetCommonTextSize()
	{
	    sMaxTextSize = -1;
	}
}
