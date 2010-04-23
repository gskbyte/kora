package org.gskbyte.kora.device;

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
        Object valobj=null;
        switch(val.type)
        {
        case Value.BOOLEAN_TYPE:
            valobj = val.getBoolean();
            break;
        case Value.INTEGER_TYPE:
            valobj = val.getInteger();
            break;
        case Value.FLOAT_TYPE:
            valobj = val.getFloat();
            break;
        }

        Log.e(TAG, "Change - " + name + " - " + valobj);
    }
}
