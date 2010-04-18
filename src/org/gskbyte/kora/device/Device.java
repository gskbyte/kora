package org.gskbyte.kora.device;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	protected String mName;
	protected Bitmap mIcon;
	protected DeviceRepresentation mRepr;
	
	// representación de controles?
	
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
	
	// operaciones para escribir en el dispositivo
}
