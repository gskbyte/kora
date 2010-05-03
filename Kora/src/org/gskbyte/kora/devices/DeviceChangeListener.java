package org.gskbyte.kora.devices;

import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.EventListener;
import org.ugr.bluerose.events.Value;

import android.util.Log;

public class DeviceChangeListener extends EventListener
{
	private static final String TAG = "DeviceChangeListener";
	
	public DeviceChangeListener()
    {
        super(DeviceChangeEvent.TOPIC_EVENT_CHANGE);
    }

    public void performAction(Event event)
    {
        String name = event.getMemberValue("name").getString();
        Value val = event.getMemberValue("value");
        Device d = DeviceManager.getDevice(name);
        d.setValue(val);
        
        Log.e(TAG, "Change - " + name + " - " + val);
    }
}
