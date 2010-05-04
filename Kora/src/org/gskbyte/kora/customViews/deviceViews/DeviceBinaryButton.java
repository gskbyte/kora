package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.devices.DeviceRepresentation.BinaryControl;
import org.ugr.bluerose.Comparison;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceBinaryButton extends KoraButton 
                                implements DeviceEventListener
{
	Device mDevice;
	DeviceRepresentation mRepr;
	BinaryControl mControl;
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
                              Device device, DeviceRepresentation dr, BinaryControl bc)
	{
		super(context);
		
		// establecer atributos iniciales de representación
        init("", null, attr);

		mDevice = device;
		mDevice.addChangeListener(this);
		
		// Iniciar vista
		mRepr = dr;
		mControl = bc;
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
			mIcon = mRepr.getStateIcon(0);
			mText = mControl.maximumTag;
		} else { // si no está al mínimo, bajar al mínimo
			mNextValue = mDevice.getMin();
            mIcon = mRepr.getStateIcon(mRepr.getStateCount()-1);
            mText = mControl.minimumTag;
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
        mDevice.removeListener(this);
    }
}
