package org.gskbyte.kora.server.clients.events;

import org.ugr.bluerose.events.Value;

public final class DeviceChangeEvent extends DeviceEvent
{
    public static final int TOPIC = 49;

    public DeviceChangeEvent(String deviceName, Value value)
    {
        init(deviceName, value, TOPIC);
    }

}
