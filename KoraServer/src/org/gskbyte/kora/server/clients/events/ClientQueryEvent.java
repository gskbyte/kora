package org.gskbyte.kora.server.clients.events;

import org.ugr.bluerose.events.Value;

public class ClientQueryEvent extends DeviceEvent
{
    static final int TOPIC = 50;
    
    public ClientQueryEvent(String deviceName, Value newValue)
    {
        init(deviceName, newValue, TOPIC);
    }
}
