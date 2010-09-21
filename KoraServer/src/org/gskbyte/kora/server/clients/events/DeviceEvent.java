package org.gskbyte.kora.server.clients.events;

import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.Value;

public abstract class DeviceEvent extends Event
{
    private static final String TAG_NAME = "name",
                                TAG_VALUE = "value";
    
    
    protected void init(String deviceName, Value value, int topic)
    {
        Value name = new Value();

        name.setString(deviceName);

        setMember(TAG_NAME, name);
        setMember(TAG_VALUE, value);
        
        this.topic = topic;
    }

    public final String getDeviceName()
    {
        Value vname = getMemberValue(TAG_NAME);
        if (vname != null) {
            return vname.getString();
        } else {
            return null;
        }
    }

    public final Value getDeviceValue()
    {
        return getMemberValue(TAG_VALUE);
    }
}
