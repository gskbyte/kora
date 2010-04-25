package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.koraSlider.KoraSlider;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.Device.DeviceEventListener;
import org.ugr.bluerose.Comparison;
import org.ugr.bluerose.events.Value;

import android.content.Context;

public class DeviceSlider extends KoraSlider implements DeviceEventListener
{
    Device mDevice;
    DeviceControl mControl;
    Value mCurrentValue;
    
    public DeviceSlider(Context context, DeviceViewAttributes attr, String deviceName, DeviceControl dc)
    {
        super(context);

        // establecer atributos iniciales de representación
        attr.textMaxSize = Attributes.TEXT_LARGE;
        init("", null, attr);
        
        mDevice = DeviceManager.getDevice(deviceName);
        mDevice.setChangeListener(this);
        mControl = dc;
        
        setView(mDevice.getValue());
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
            //mNextValue = mDevice.getMax();
            mIcon = mControl.getIcon(((DeviceViewAttributes)mAttrs).icon, 0);
            mText = mControl.getStateAbsoluteAction(mControl.getStateCount()-1);
            mText="Apagada";
        } else { // si no está al mínimo, bajar al mínimo
            //mNextValue = mDevice.getMin();
            mIcon = mControl.getIcon(((DeviceViewAttributes)mAttrs).icon, mControl.getStateCount()-1);
            mText = mControl.getStateAbsoluteAction(0);
            mText="Encendida";
        }
        invalidate();
    }
    
    
    @Override
    public void onDeviceChange(Value newVal)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        
    }

}
