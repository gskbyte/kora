package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.customViews.KoraView;
import org.gskbyte.kora.device.DeviceRepresentation;

public class DeviceViewAttributes extends KoraButton.Attributes
{
    // Valores propios de configuración de este botón
    public int icon = DeviceRepresentation.ICON_DEFAULT;
    public boolean customIcon = false;
    
    public DeviceViewAttributes()
    {
        super();
    }
    
    public DeviceViewAttributes(KoraView.Attributes o)
    {
        super(o);
    }
    
    public DeviceViewAttributes(DeviceViewAttributes o)
    {
        super(o);
        icon = o.icon;
        customIcon = o.customIcon;
    }
}
