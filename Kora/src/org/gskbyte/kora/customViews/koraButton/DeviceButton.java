package org.gskbyte.kora.customViews.koraButton;

import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.handling.DeviceHandlingActivity;
import org.gskbyte.kora.handling.DeviceSelectionActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DeviceButton extends KoraButton
{
	String mDeviceName;
	
	public static class Attributes extends KoraButton.Attributes
    {
		// Valores propios de configuración de este botón
		public int icon = DeviceRepresentation.ICON_DEFAULT;
		public boolean customIcon = false;
    }
	
	public DeviceButton(Context context, Attributes attr,
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
		mText = d.getName();
		
		// otras propiedades
        mFocused = mSelected = false;
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
