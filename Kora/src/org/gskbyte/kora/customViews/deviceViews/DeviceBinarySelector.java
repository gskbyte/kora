package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.devices.DeviceRepresentation.BinaryControl;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceBinarySelector extends GridLayout implements DeviceEventListener
{
    protected Device mDevice;
    
    public DeviceBinarySelector(Context context, 
            DeviceViewAttributes attr1, DeviceViewAttributes attr2, 
            Device device, DeviceRepresentation repr, BinaryControl bc)
    {
        super(context);
        
        mDevice = device;
        
        KoraButton b0 = new KoraButton(context,
                                       bc.minimumTag,
                                       repr.getStateIcon(0),
                                       attr1);

        KoraButton b1 = new KoraButton(context,
                                       bc.maximumTag,
                                       repr.getStateIcon(repr.getStateCount()-1),
                                       attr2);
        
        
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

        setDimensions(1, 2);
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
