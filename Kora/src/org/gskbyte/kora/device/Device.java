package org.gskbyte.kora.device;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	protected String mName;
	protected Object mCurrentValue;
	protected DeviceRepresentation mRepr;
	//protected Bitmap mCustomIcon; // alguna vez se usará
	
	public Device(String name, DeviceSpec s, DeviceRepresentation dr)
	{
	    super(s);
	    mName = name;
	    mRepr = dr;
	}
	
	public String getName()
	{
	    return mName;
	}
	
	public Bitmap getIcon(int which)
	{
		return mRepr.getIcon(which);
	}
	
	public DeviceRepresentation getDeviceRepresentation()
	{
		return mRepr;
	}
	
	public Object getValue()
	{
		return mCurrentValue;
	}
	
	public void setValue(Object newValue)
	{
		mCurrentValue = newValue;
	}
	
	// operaciones para escribir en el dispositivo
}
