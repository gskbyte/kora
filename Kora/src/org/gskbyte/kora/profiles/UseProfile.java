package org.gskbyte.kora.profiles;

public class UseProfile extends Profile
{
    private static final long serialVersionUID = 456454165165798L;

    // Interaction modes
    public class interaction{
        public static final int touch_mode = 0;	
            //public static final int multitouch_and_drag = 0;
            public static final int press_and_drag = 1;
            public static final int simple_press = 2;
        
        public static final int scan_mode = 1;
            public static final int simple_scan = 0;
            public static final int row_column_scan = 1;
        
        public static final int no_voice = 0;
        // more voice modes here

        public static final int pagination_standard = 0;
        public static final int pagination_automatic = pagination_standard; // alias for scan mode
        public static final int pagination_buttons = 1;
        public static final int pagination_manual = pagination_buttons; // alias for scan mode
    }
    
    // Visualization modes
    public class visualization{
        public static final int view_standard = 0;
        public static final int view_plain_color = 1;
        public static final int view_plain_differenced_color = 2;
        public static final int view_hi_contrast_color = 3;
        public static final int view_black_and_white = 4;
        
        public static final int orientation_both = 0;
        public static final int orientation_vertical = 1;
        public static final int orientation_horizontal = 2;
        
        public static final int margin_small = 0;
        public static final int margin_medium = 1;
        public static final int margin_large = 2;

        public static final int text_size_small = 0;
        public static final int text_size_medium = 1;
        public static final int text_size_large = 2;
        
        public static final int font_sans = 0;
        public static final int font_masallera = 1;
        public static final int font_monofur = 2;
        
        public static final int icon_pictogram = 0;
        public static final int icon_high_contrast = 1;
        public static final int icon_black_white = 2;
        public static final int icon_photo = 3;
        public static final int icon_animation = 4;
    }
    
    public class feedback{
        public static final int content_highlight_none = 0;
        public static final int content_highlight_standard = 1;
        public static final int content_highlight_zoom = 2;
        public static final int content_highlight_increase_brightness = 3;
        
    }
    
    public class sound{
        public static final int no_sounds = 0;
        public static final int simple_sounds = 1;
        public static final int voice_sounds = 2;
        
        public static final int voice_default = 0;
        public static final int voice_custom = 1;
    }
    // Interaction settings
    public int mainInteraction = interaction.touch_mode;
    public int touchMode = interaction.press_and_drag;
    public int scanMode = interaction.simple_scan;
    public int scanTimeMillis = 2500;
    public int paginationMode = interaction.pagination_standard;
    public int voiceInteraction = interaction.no_voice;
    
    // Visualization settings
    public int viewMode = visualization.view_standard;
    public int backgroundColor = 0xFF000000;
    public int rows = 2;
    public int columns = 2;
    public int margin = visualization.margin_small;
    public int orientations = visualization.orientation_both;
    public boolean showText = true;
    public int textSize = visualization.text_size_small;
    public int typography = visualization.font_sans;
    public boolean textCaps = false;
    public int textColor = 0xFF000000;
    public int iconMode = visualization.icon_pictogram;
    public boolean customImage = false;
    
    // Feedback settings
    public boolean vibration = false;
    public boolean confirmation = false;
    public int confirmationTimeMillis = 3000;
    public int contentHighlight = feedback.content_highlight_standard;
    public boolean borderHighlight = false;
    
    // Sound settings
    public int soundMode = sound.no_sounds;
    public boolean soundOnSelection = false;
    public boolean soundOnAction = false;
    public int voiceMode = sound.voice_default;
    
    public UseProfile(String name)
    {
        this.name = name;
    }
}
