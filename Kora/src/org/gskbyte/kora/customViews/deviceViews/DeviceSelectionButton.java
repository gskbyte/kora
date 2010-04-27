package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.handling.DeviceHandlingActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DeviceSelectionButton extends KoraButton
{
	String mDeviceName;
	
	public DeviceSelectionButton(Context context, DeviceViewAttributes attr,
						 String deviceName)
	{
		super(context);
		
		Device d = DeviceManager.getDevice(deviceName);
		
		mDeviceName = d.getSystemName();
		// establecer atributos de representación
		mAttrs = attr;
		
		// establecer icono
		if(attr.customIcon){
			; // si alguna vez entro, avisen!
		} else {
			mIcon = d.getIcon(attr.icon);
		}
		
		// establecer nombre (traducido)
		mText = d.getReadableName();
		
		// otras propiedades
        mFocused = mBlocked = false;
        setFocusable(true);
        setClickable(true);
        
        // añadir listener que llame a la actividad de manejo correspondiente
        View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), DeviceHandlingActivity.class);
				i.putExtra(DeviceHandlingActivity.TAG_DEVICE_NAME, mDeviceName);
				
				getContext().startActivity(i);
			}
		};
        setOnClickListener(l);
	}
	
	
	

}
