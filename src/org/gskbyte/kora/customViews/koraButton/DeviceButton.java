package org.gskbyte.kora.customViews.koraButton;

import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;

import android.content.Context;

public class DeviceButton extends KoraButton
{
	String mDeviceName;
	
	public static class Attributes extends KoraButton.Attributes
    {
		// Valores propios de configuración de este botón
		public int icon = DeviceRepresentation.ICON_DEFAULT;
		public boolean custom_icon = false;
    }
	
	public DeviceButton(Context context, Attributes attr,
						 String deviceName)
	{
		super(context);
		
		Device d = DeviceManager.getDevice(deviceName);
		
		// establecer atributos de representación
		mAttrs = attr;
		
		// establecer icono
		if(attr.custom_icon){
			; // si alguna vez entro, avisen!
		} else {
			mIcon = d.getIcon(attr.icon);
		}
		
		// establecer nombre (traducido)
		mText = d.getName();
		
		// otras propiedades
        mFocused = mSelected = false;
        setFocusable(true);
        setClickable(true);
        
        // añadir listener que llame a la actividad de manejo correspondiente
        
        
	}
	
	
	

}
