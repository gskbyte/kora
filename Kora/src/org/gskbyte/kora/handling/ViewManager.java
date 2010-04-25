package org.gskbyte.kora.handling;

import org.gskbyte.kora.customViews.KoraView;
import org.gskbyte.kora.customViews.deviceViews.DeviceViewAttributes;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

public class ViewManager
{
    protected static Context sContext;
    
    public static final int FONT_DEFAULT = 0,
                            FONT_MASALLERA = 1,
                            FONT_MONOFUR = 2;
    
    public static final int COLOR_INDEX_BACK = -3,
                            COLOR_INDEX_PREVIOUS = -2,
                            COLOR_INDEX_NEXT = -1;
    
    protected static DeviceViewAttributes sAttr;
    protected static UseProfile sUp;
    
    
    protected static final int white =    Color.rgb(255, 255, 255),
                               greyBg =   Color.rgb(150, 150, 150),
                               greyBd =   Color.rgb( 75,  75,  75),
                               black =    Color.rgb(0, 0, 0),
                               defColor = Color.rgb(79, 150, 215);
    // 19 elementos: 16 colores + 3 para botones de paginación y vuelta
    protected static final int[] sBgPalette = {
                                Color.rgb(237, 119,  96), // salmón
                                Color.rgb( 84, 224, 199), // aguamarina claro
                                Color.rgb(248, 243, 108), // amarillo claro
                                Color.rgb(250, 178, 253), // rosa claro
                                Color.rgb(192, 224,  73), // verde pera
                                Color.rgb(184, 158, 103), // marrón claro
                                Color.rgb(146, 255, 164), // verde intenso claro
                                Color.rgb(190, 190, 190), // gris claro
                                
                                // Se repiten
                                Color.rgb(237, 119,  96), // salmón
                                Color.rgb( 84, 224, 199), // aguamarina
                                Color.rgb(248, 243, 108), // amarillo claro
                                Color.rgb(250, 178, 253), // rosa claro
                                Color.rgb(192, 224,  73), // verde pera
                                Color.rgb(184, 158, 103), // marrón claro
                                Color.rgb(146, 255, 164), // verde intenso claro
                                Color.rgb(190, 190, 190), // gris claro
                                
                            };
    
    protected static final int[] sBgPaletteSpecial = {
                                Color.rgb( 89, 159, 237), // azul claro
                                Color.rgb(244, 166, 147), // naranja rojizo claro
                                Color.rgb(116, 189, 109), // verde claro mate
                            };
    
    // Mismos colores que arriba, un poco más intensos
    protected static final int[] sBgPalette2 = {
                                Color.rgb(229,  98,  26),
                                Color.rgb( 14, 212, 177),
                                Color.rgb(239, 232,  52),
                                Color.rgb(226, 106, 234),
                                Color.rgb(147, 181,  30),
                                Color.rgb(150, 118,  50),
                                Color.rgb( 85, 200, 106),
                                Color.rgb(150, 150, 150),
                                
                                Color.rgb(229,  98,  26),
                                Color.rgb( 14, 212, 177),
                                Color.rgb(239, 232,  52),
                                Color.rgb(147, 181,  30),
                                Color.rgb(226, 106, 234),
                                Color.rgb(150, 118,  50),
                                Color.rgb( 85, 200, 106),
                                Color.rgb(150, 150, 150),
                            };
    
    protected static final int[] sBgPaletteSpecial2 = {
                                Color.rgb( 54, 122, 198),
                                Color.rgb(219,  86,  55),
                                Color.rgb( 76, 159,  67),
                            };
    
    protected static final int[] sBgHcPalette = {
                                Color.rgb(255, 158,   4), // naranja
                                Color.rgb(  4, 255, 240), // aguamarina
                                Color.rgb(255, 255,   0), // amarillo
                                Color.rgb(255,   0, 255), // rosa
                                Color.rgb( 39, 169,  13), // verde oscuro
                                Color.rgb(135, 109,   6), // marrón
                                Color.rgb( 88,   6, 135), // morado
                                Color.rgb( 60,  60,  60), // gris
                                
                                // se repiten
                                Color.rgb(255, 158,   4), // naranja
                                Color.rgb(  4, 255, 240), // aguamarina
                                Color.rgb(255, 255,   0), // amarillo
                                Color.rgb(255,   0, 255), // rosa
                                Color.rgb( 39, 169,  13), // verde oscuro
                                Color.rgb(135, 109,   6), // marrón
                                Color.rgb( 88,   6, 135), // morado
                                Color.rgb( 60,  60,  60), // gris
                            };
    protected static final int[] sBgHcPaletteSpecial = {
                                Color.rgb( 28,  43, 244), // azul brillante
                                Color.rgb(255,  15,   4), // rojo brillante
                                Color.rgb(  4, 243,  15), // verde brillante
                            };
    
    protected static Typeface [] sTypefaces = {null, null, null};
    
    protected static void init(Context ctx)
    {
        sContext = ctx;
        sUp = SettingsManager.getCurrentUseProfile();
        sAttr = new DeviceViewAttributes();
        
        // Opciones de vibración, orientación y demás (DESACTIVAR AL SALIR)
        
        
        
        
        // Propiedades de botones
            // viewMode
            // backColor
        
        // showText
        sAttr.showText = sUp.showText;
        
        // fontSize
        switch(sUp.textSize){
        case UseProfile.visualization.text_size_large:
            sAttr.textMaxSize = KoraButton.Attributes.TEXT_LARGE;
            break;
        case UseProfile.visualization.text_size_medium:
            sAttr.textMaxSize = KoraButton.Attributes.TEXT_MEDIUM;
            break;
        case UseProfile.visualization.text_size_small:
            sAttr.textMaxSize = KoraButton.Attributes.TEXT_SMALL;
        default:
            break;
        }
        
        // OJO: fullería, xq se corresponde con los índices!
        sAttr.typeface = getTypeFace(sUp.typography);
            // typographyCaps
        
        // textColor
        sAttr.textColor = sUp.textColor;
        
            
            // customImage
        
        
        
        sAttr.vibrate = sUp.vibration;
            // confirmation
            // confirmationTimeMillis
            // contentHighlight
            // borderHighlight
        
        /* TTS
         * http://android-developers.blogspot.com/2009/09/introduction-to-text-to-speech-in.html
         * */
    }
    
    public static DeviceViewAttributes getAttributes()
    {
        return sAttr;
    }
    
    public static DeviceViewAttributes getAttributes(int index)
    {
        DeviceViewAttributes a = new DeviceViewAttributes(sAttr);
        setColors(index, a);
        return a;
    }
    
    public static DeviceViewAttributes getAttributes(int index, boolean overrideTextSize)
    {
        DeviceViewAttributes a = getAttributes(index);
        a.overrideMaxSize = overrideTextSize;
        return a;
    }
    
    protected static void loadTypeface(int id)
    {
        switch(id){
        case FONT_DEFAULT:
            sTypefaces[id] = Typeface.DEFAULT;
            break;
        case FONT_MASALLERA:
            sTypefaces[id] = Typeface.createFromAsset(sContext.getAssets(), "fonts/massallera.ttf"); 
            break;
        case FONT_MONOFUR:
            sTypefaces[id] = Typeface.createFromAsset(sContext.getAssets(), "fonts/monofur.ttf"); 
            break;
        }
    }
    
    public static Typeface getTypeFace(int id)
    {
        if(sTypefaces[id]==null)
            loadTypeface(id);
        return sTypefaces[id];
    }
    
    public static void setColors(int index, DeviceViewAttributes attr){
        switch(sUp.viewMode){
        case UseProfile.visualization.view_plain_differenced_color:
            if(index>=0){
                attr.backgroundColors[0] = sBgPalette[index%16];
                attr.backgroundColors[1] = sBgPalette[index%16];
                attr.backgroundColors[2] = sBgPalette2[index%16];
                
                attr.borderColors[0] = sBgPalette[index%16];
                attr.borderColors[1] = sBgPalette2[index%16];
                attr.borderColors[2] = sBgPalette2[index%16];
            } else {
                attr.backgroundColors[0] = sBgPaletteSpecial[3+index];
                attr.backgroundColors[1] = sBgPaletteSpecial[3+index];
                attr.backgroundColors[2] = sBgPaletteSpecial2[3+index];
                
                attr.borderColors[0] = sBgPaletteSpecial[3+index];
                attr.borderColors[1] = sBgPaletteSpecial2[3+index];
                attr.borderColors[2] = sBgPaletteSpecial2[3+index];
            }
            break;
        case UseProfile.visualization.view_hi_contrast_color:
            if(index>=0){
                attr.backgroundColors[0] = sBgHcPalette[index%16];
                attr.backgroundColors[1] = sBgHcPalette[index%16];
                
                attr.borderColors[0] = sBgHcPalette[index%16];
            } else {
                attr.backgroundColors[0] = sBgHcPaletteSpecial[3+index];
                attr.backgroundColors[1] = sBgHcPaletteSpecial[3+index];
                
                attr.borderColors[0] = sBgHcPaletteSpecial[3+index];
            }
            attr.backgroundColors[2] = white;
            
            attr.borderColors[1] = white;
            attr.borderColors[2] = white;
            break;
            //return sBgHcPalette[index];
        case UseProfile.visualization.view_plain_color:
            int color = sUp.backgroundColor;
            
            float [] hsv = {0,0,0};
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.85;
            int color2 = Color.HSVToColor(hsv);
            
            attr.backgroundColors[0] = color;
            attr.backgroundColors[1] = color;
            attr.backgroundColors[2] = color2;
            
            attr.borderColors[0] = color;
            attr.borderColors[1] = color2;
            attr.borderColors[2] = color2;
            break;
        case UseProfile.visualization.view_black_and_white:
            attr.backgroundColors[0] = white;
            attr.backgroundColors[1] = white;
            attr.backgroundColors[2] = greyBg;
            
            attr.borderColors[0] = white;
            attr.borderColors[1] = greyBd;
            attr.borderColors[2] = greyBd;
            break;
        case UseProfile.visualization.view_standard:
        default:
            attr.backgroundColors[0] = white;
            attr.backgroundColors[1] = white;
            attr.backgroundColors[2] = defColor;
            
            attr.borderColors[0] = white;
            attr.borderColors[1] = defColor;
            attr.borderColors[2] = defColor;
            break;
        }
    }
}
