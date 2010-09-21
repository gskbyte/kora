package org.gskbyte.kora.server.devices.knx;

import org.gskbyte.kora.server.devices.DeviceValue;

import tuwien.auto.calimero.datapoint.StateDP;

public class KNXBooleanDevice extends KNXDevice
{
    private final StateDP mDp = new StateDP(mAddress, "", 0, "1.002");
    
	public KNXBooleanDevice(String systemName, String readableName, 
            String deviceType, int accessMode, String address) throws Exception
    {
    	super(systemName, readableName,  deviceType, accessMode, address);
    	readValue();
    }
	
	void readValue() throws Exception 
    {
	    String s = comm.read(mDp);
	    boolean b = Boolean.parseBoolean(s);
	    value = new DeviceValue(b);
    }
	
	public boolean get()
	{
		return value.getCurrent().getBoolean();
	}
	
	public void set(boolean b) throws Exception
	{
	    comm.write(mDp, b ? "true" : "false");
	    value.setCurrent(b);
	}
}
