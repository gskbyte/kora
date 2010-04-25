package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.Device.DeviceEventListener;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceBinarySelector extends GridLayout implements DeviceEventListener
{
    protected Device mDevice;
    
    public DeviceBinarySelector(Context context, DeviceViewAttributes attr, String deviceName, DeviceControl dc)
    {
        super(context);
        setDimensions(1, 2);
        
        KoraButton b0 = new KoraButton(context,
                                       dc.getStateAbsoluteAction(0),
                                       dc.getIcon(attr.icon, 0));

        KoraButton b1 = new KoraButton(context,
                                       dc.getStateAbsoluteAction(1),
                                       dc.getIcon(attr.icon, 1));
        
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
