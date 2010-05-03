package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.handling.DeviceHandlingActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

public class DeviceSelectionButton extends KoraButton
{
	String mDeviceName;
	
	public DeviceSelectionButton(Context context, DeviceViewAttributes attr,
						 String deviceName)
	{
		super(context);
		
		// Get device and add listener to call handling activity
		Device d = DeviceManager.getDevice(deviceName);
		mDeviceName = d.getSystemName();
		View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DeviceHandlingActivity.class);
                i.putExtra(DeviceHandlingActivity.TAG_DEVICE_NAME, mDeviceName);
                
                getContext().startActivity(i);
            }
        };
        setOnClickListener(l);
        
		// Init view attributes
        Bitmap icon =  d.getIcon(attr.icon); // cargar personalizado aquí cuando lo haya
		init(d.getReadableName(), icon, attr);
	}
}
