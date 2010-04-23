package org.gskbyte.kora.device;

public class DeviceQueryEvent extends DeviceChangeEvent
{
    private static final int TOPIC_EVENT_QUERY = 50;
    
	public DeviceQueryEvent(String deviceName, int type, Object newValue)
	{
		super(deviceName, type, newValue);
		type = TOPIC_EVENT_QUERY;
	}
}
