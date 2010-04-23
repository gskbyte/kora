package org.gskbyte.kora.device;

import org.ugr.bluerose.events.Value;

public class DeviceQueryEvent extends DeviceChangeEvent
{
    static final int TOPIC_EVENT_QUERY = 50;
    
	public DeviceQueryEvent(String deviceName, Value newValue)
	{
		super(deviceName, newValue);
		topic = TOPIC_EVENT_QUERY;
	}
}
