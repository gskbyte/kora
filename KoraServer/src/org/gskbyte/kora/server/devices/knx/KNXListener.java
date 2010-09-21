package org.gskbyte.kora.server.devices.knx;

import org.gskbyte.kora.server.clients.events.DeviceChangeEvent;
import org.gskbyte.kora.server.devices.Device;
import org.gskbyte.kora.server.devices.DeviceManager;
import org.gskbyte.kora.server.util.Log;
import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.Value;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListenerEx;

public class KNXListener extends ProcessListenerEx
{
    public KNXListener()
    {
        
    }
    
    @Override
    public void detached(DetachEvent arg0)
    { 
        
    }

    @Override
    public void groupWrite(ProcessEvent evt)
    {
        final String source = evt.getSource().toString();
        final String destination = evt.getDestination().toString();
        final String devName = KNXDevice.addrMap.get(destination);
        final KNXDevice dev = (KNXDevice) DeviceManager.instance().getDevice(devName);
        
        if(dev != null){
            /*try {
                dev.readValue();
            } catch (Throwable e) {
                Log.log("Error reading from device "+devName);
            }*/
            Event e = new Event(DeviceChangeEvent.TOPIC);

            Value deviceName = new Value(),
                  deviceValue = new Value();
            
            deviceName.setString(devName);

            try {
                switch(dev.getValue().type){
                case Value.BOOLEAN_TYPE:
                    final boolean b = asBool(evt);
                    deviceValue.setBoolean(b);
                    dev.setValue(deviceValue);
                    Log.log("New boolean value for "+devName+": "+b);
                    break;
                case Value.INTEGER_TYPE:
                    final int i = asUnsigned(evt, "5.001");
                    deviceValue.setInteger(i);
                    Log.log("New integer value for "+devName+": "+i);
                    break;
                }
                
                e.setMember("name", deviceName);
                e.setMember("value", deviceValue);
                
                org.ugr.bluerose.events.EventHandler.publish(e, false);
            } catch (KNXFormatException e1) {
                Log.log("KNXFormatException: "+e1.getMessage());
            } catch (Exception e2) {
                Log.log("KNXException: "+e2.getMessage());
            }
        }
    }

    @Override
    public void groupReadRequest(ProcessEvent arg0)
    {
        /*
        System.out.println("groupReadReq " + arg0.getSourceAddr().toString() + " - " + 
                arg0.getDestination().toString() + " : " +
                arg0.getASDU());
                */
    }

    @Override
    public void groupReadResponse(ProcessEvent arg0)
    {
        /*System.out.println("groupReadResp " + arg0.getSourceAddr().toString() + " - " + 
                arg0.getDestination().toString() + " : " +
                arg0.getASDU());*/
    }
}
