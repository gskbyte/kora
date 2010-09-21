package org.gskbyte.kora.server.clients.events;

import org.gskbyte.kora.server.devices.Device;
import org.gskbyte.kora.server.devices.DeviceManager;
import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.EventListener;
import org.ugr.bluerose.events.Value;

public class ClientChangeEventListener extends EventListener
{
    public ClientChangeEventListener()
    {
        super(DeviceChangeEvent.TOPIC);
    }

    public void performAction(Event event)
    {
        String name = event.getMemberValue("name").getString();
        Value val = event.getMemberValue("value");

        System.out.println("EVENTO -> "+name + " "+val.getBoolean());
        
        Device dev = DeviceManager.instance().getDevice(name);
        try {
            dev.setValue(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
