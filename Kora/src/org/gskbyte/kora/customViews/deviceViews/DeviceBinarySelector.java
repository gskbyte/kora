package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceControl;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceBinarySelector extends GridLayout implements DeviceEventListener
{
    protected Device mDevice;
    
    public DeviceBinarySelector(Context context, 
            DeviceViewAttributes attr1, DeviceViewAttributes attr2, 
            String deviceName, DeviceControl dc)
    {
        super(context);
        setDimensions(1, 2);
        
        KoraButton b0 = new KoraButton(context,
                                       dc.getStateAbsoluteAction(0),
                                       dc.getIcon(attr1.icon, 0),
                                       attr1);

        KoraButton b1 = new KoraButton(context,
                                       dc.getStateAbsoluteAction(1),
                                       dc.getIcon(attr2.icon, 1),
                                       attr2);
        
        mDevice = DeviceManager.getDevice(deviceName);
        
        b0.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    DeviceManager.setValueForDevice(mDevice.getSystemName(), mDevice.getMin());
                }
            });
        
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                DeviceManager.setValueForDevice(mDevice.getSystemName(), mDevice.getMax());
            }
        });
        
        addView(b0);
        addView(b1);
    }

    @Override
    public void onDeviceChange(String deviceName, Value newVal)
    {
        ;
    }

    @Override
    public void unregister()
    {
        ;
    }
}
