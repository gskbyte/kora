package org.gskbyte.kora.settings;

public class UseProfile
{
    // Interaction modes
    public class interaction{
        public static final int touch_mode = 0x0000;	
        public static final int multitouch_and_drag = 0x0001;
        public static final int touch_and_drag = 0x0002;
        public static final int single_touch = 0x0003;
        
        public static final int scan_mode = 0x0100;
        
        public static final int no_voice = 0x0200;
        // more voice modes here
    }
    
    // Visualization modes
    public class visualization{
        public static final int view_standard = 0x1000;
        public static final int view_plain_color = 0x1001;
        public static final int view_plain_differenced_color = 0x1002;
        public static final int view_hi_contrast_color = 0x1003;
        public static final int view_black_and_white = 0x1004;
        
        public static final int text_font_sans = 0x1100;
        public static final int calligraphic_font = 0x1101;
        public static final int caps_font = 0x1102;
        
        public static final int icon_pictogram = 0x1200;
        public static final int icon_photo = 0x1201;
        public static final int icon_animation = 0x1202;
        
        public static final int pagination_standard = 0x1300;
        public static final int pagination_automatic = pagination_standard; // alias for scan mode
        
        public static final int pagination_buttons = 0x1301;
        public static final int pagination_manual = pagination_buttons; // alias for scan mode
    }
    
    public class feedback{
        public static final int content_highlight_none = 0x2401;
        public static final int content_highlight_zoom = 0x2401;
        public static final int content_highlight_increase_brightness = 0x2402;
        
    }
    
    public class sound{
        public static final int no_sounds = 0x3000;
        public static final int simple_sounds = 0x3001;
        public static final int voice_sounds = 0x3002;
        
    }
    // General settings
    public String name = "";
    public boolean isDefaultProfile = false;
    
    // Interaction settings
    public int mainInteraction = interaction.touch_mode;
    public int touchMode = interaction.multitouch_and_drag;
    public int focusTimeMillis = 2500;
    public int voiceInteraction = interaction.no_voice;
    
        
    // Visualization settings
    public int viewMode = visualization.view_standard;
    public int rows = 2;
    public int columns = 2;
    public boolean text = true;
    public int fontSize = 20;
    public int typography = visualization.text_font_sans;
    public int iconMode = visualization.icon_pictogram;
    public int paginationMode = visualization.pagination_standard;
    
    // Feedback settings
    public boolean vibration = false;
    public boolean confirmation = false;
    public int confirmationTimeMillis = 5000;
    public int contentHighlight = feedback.content_highlight_none;
    public boolean borderHighlight = false;
    
    // Sound settings
    public int soundMode = sound.no_sounds;
    public boolean soundOnSelection = true;
    public boolean soundOnHover = true;
    
    
    
    public UseProfile(){
        
    }
}
