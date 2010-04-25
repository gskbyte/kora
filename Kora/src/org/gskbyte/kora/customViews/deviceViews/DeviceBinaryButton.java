package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.Device.DeviceEventListener;
import org.ugr.bluerose.Comparison;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceBinaryButton extends KoraButton 
                                implements DeviceEventListener
{
	Device mDevice;
	DeviceControl mControl;
	Value mCurrentValue, mNextValue;
	
	/* Necesario para actualizar la vista en la hebra principal */
	private class ResultUpdater implements Runnable
    {
	    Value mNextValue;
        
        public ResultUpdater(Value nextValue){
            mNextValue = nextValue;
        }
        
        public void run(){
            DeviceBinaryButton.this.setView(mNextValue);
        }
    }
    private final android.os.Handler mViewUpdaterHandler = new android.os.Handler();
	
	public DeviceBinaryButton(Context context, DeviceViewAttributes attr, 
	                          String deviceName, DeviceControl dc)
	{
		super(context);
		
		// establecer atributos iniciales de representación
        init("", null, attr);

		mDevice = DeviceManager.getDevice(deviceName);
		mDevice.setChangeListener(this);
		mControl = dc;
		
		setView(mDevice.getValue());
        
        View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DeviceManager.setValueForDevice(mDevice.getSystemName(), mNextValue);
				setView(mNextValue);
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
			mIcon = mControl.getIcon(((DeviceViewAttributes)mAttrs).icon, 0);
			mText = mControl.getStateAbsoluteAction(mControl.getStateCount()-1);
		} else { // si no está al mínimo, bajar al mínimo
			mNextValue = mDevice.getMin();
			mIcon = mControl.getIcon(((DeviceViewAttributes)mAttrs).icon, mControl.getStateCount()-1);
			mText = mControl.getStateAbsoluteAction(0);
		}
		invalidate();
	}
	
	@Override
	public void onDeviceChange(String deviceName, Value newVal)
	{
	    if(deviceName.equals(mDevice.getSystemName()))
	        mViewUpdaterHandler.post(new ResultUpdater(newVal));
	}

    @Override
    public void unregister()
    {
        mDevice.unsetListener();
    }
}
