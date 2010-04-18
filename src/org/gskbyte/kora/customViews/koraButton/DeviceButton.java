package org.gskbyte.kora.customViews.koraButton;

import org.gskbyte.kora.device.DeviceManager;

import android.content.Context;

public class DeviceButton extends KoraButton
{
	String mDeviceName;
	
	private DeviceButton(Context context, Attributes attr,
						 String deviceName)
	{
		super(context);
		
		// establecer atributos de representación
		mAttrs = attr;
		
		// establecer icono
		
		
		// establecer nombre (traducido)
		mText = DeviceManager.getDevice(deviceName).getName();
		
		// otras propiedades
        mFocused = mSelected = false;
        setFocusable(true);
        setClickable(true);
        
        // añadir listener que llame a la actividad de manejo correspondiente
        
	}
	
	
	

}
