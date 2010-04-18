package org.gskbyte.kora.device;

import java.util.HashMap;
import java.util.Vector;

import android.graphics.Bitmap;

public class DeviceRepresentation
{
    public static final int ICON_DEFAULT = 0,
                            ICON_HIGH_CONTRAST = 1,
                            ICON_BLACK_WHITE = 2,
                            ICON_ANIMATION = 3,
                            ICON_PHOTO = 4;

    protected String mSystemName;
    protected String mName;
    
    protected Vector<Bitmap> icons = new Vector<Bitmap>(5);
    protected HashMap<String, AbstractDeviceControl> mControls = 
        new HashMap<String, AbstractDeviceControl>();
    
    public DeviceRepresentation(String assetFolder)
    {
        
    }
    
    public DeviceRepresentation(/* XML Object*/)
    {
        
    }
    
    public String getSystemName()
    {
        return mSystemName;
    }
    
    public Bitmap getIcon(int which)
    {
        if(which<icons.size()){
            Bitmap r = icons.get(which);
            if(r != null)
                return r;
        }
        
        return icons.get(ICON_DEFAULT);
    }
}
