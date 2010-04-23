package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.device.Device.DeviceChangeListener;
import org.gskbyte.kora.handling.DeviceHandlingActivity;
import org.ugr.bluerose.Comparison;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DeviceBinaryButton extends KoraButton implements DeviceChangeListener
{
	Device mDevice;
	DeviceControl mControl;
	Value mCurrentValue, mNextValue;
	
	public static class Attributes extends KoraButton.Attributes
    {
		// Valores propios de configuración de este botón
		public int icon = DeviceRepresentation.ICON_DEFAULT;
    }
	
	public DeviceBinaryButton(Context context, Attributes attr, String deviceName, String controlName)
	{
		super(context);
		
		// establecer atributos de representación
		mAttrs = attr;

		mDevice = DeviceManager.getDevice(deviceName);
		mDevice.setChangeListener(this);
		
		mControl = mDevice.getRepresentation().getControl(controlName);
		
		setView(mDevice.getValue());
		
		// establecer nombre (traducido)
		mText = mDevice.getReadableName();
		
		// otras propiedades
        mFocused = mSelected = false;
        setFocusable(true);
        setClickable(true);
        
        // añadir listener que llame a la actividad de manejo correspondiente
        View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DeviceManager.setValueForDevice(mDevice.getSystemName(), mNextValue);
			}
		};
        setOnClickListener(l);
	}
	
	protected void setView(Value currentValue)
	{
		// Obtener valor actual del dispositivo
		mCurrentValue = currentValue;
		
		// Establecer el valor a poner con la pulsación
		// Establecer icono actual
		// Establecer texto
		Value minimumValue = mDevice.getMin();
		boolean isMinimum = currentValue.compare(Comparison.EQUAL, minimumValue);
		if(isMinimum){
			mNextValue = mDevice.getMax();
			mIcon = mControl.getIcon(((Attributes)mAttrs).icon, 0);
			mText = mControl.getCaption(0);
		} else { // si no está al mínimo, bajar al mínimo
			mNextValue = mDevice.getMin();
			mIcon = mControl.getIcon(((Attributes)mAttrs).icon, 1);
			mText = mControl.getCaption(1);
		}
	}
	
	@Override
	public void onDeviceChange(Value newVal)
	{
		setView(newVal);
	}
}
