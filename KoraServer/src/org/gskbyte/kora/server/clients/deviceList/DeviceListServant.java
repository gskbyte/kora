package org.gskbyte.kora.server.clients.deviceList;

import java.util.Collection;
import java.util.Vector;

import org.gskbyte.kora.server.devices.Device;
import org.gskbyte.kora.server.devices.DeviceManager;
import org.gskbyte.kora.server.util.Log;
import org.ugr.bluerose.MethodResult;

/**
*
* @author jose
*/
public class DeviceListServant extends org.ugr.bluerose.ObjectServant {

    public static final String TAG_NAME = "Kora",
            TAG_CATEGORY = "Domotics";
    public static final String TAG_OPERATION = "getDeviceSpecs";

    public DeviceListServant()
    {
        super();
        identity.id_name = TAG_NAME;
        identity.category = TAG_CATEGORY;
        System.out.println("Crear servicio");
    }
    
    @Override
    public MethodResult runMethod(String method, String userId,
            Vector<Byte> args)
    {
        MethodResult result = new MethodResult();
        
        Log.log("runMethod");
        
        if (method.equals(TAG_OPERATION)) {
            Collection<Device> res = DeviceManager.instance().getAll();

            synchronized(mutex) {
                writer.writeSize(res.size());
                
                Log.log("Number of devices returned: "+res.size());
                for (Device d : res) {
                    d.marshall(writer);
                }

                result.result = writer.toVector();
                
                writer.reset();
            }
            
            result.status =
                org.ugr.bluerose.messages.MessageHeader.SUCCESS_STATUS;

            return result;
        }

        result.status = org.ugr.bluerose.messages.MessageHeader.OPERATION_NOT_EXIST_STATUS;
        return result;
    }
}
