package simkora.device;

import java.util.Vector;
import org.ugr.bluerose.MethodResult;

import simkora.SimKoraView;

/**
*
* @author jose
*/
public class DeviceListServant extends org.ugr.bluerose.ObjectServant {

    public static final String TAG_NAME = "Kora",
            TAG_CATEGORY = "Domotics";
    public static final String TAG_OPERATION = "getDeviceSpecs";

    public DeviceListServant() {
        super();
        identity.id_name = TAG_NAME;
        identity.category = TAG_CATEGORY;
        System.out.println("Crear servicio");
        DeviceManager.init();
    }
    
    @Override
    public MethodResult runMethod(String method, String userId,
            Vector<Byte> args)
    {
        MethodResult result = new MethodResult();
        
        SimKoraView.log(method + " called from " + userId);
        
        if (method.equals(TAG_OPERATION)) {
            java.util.Vector<DeviceSpec> res = getDeviceSpecs();

            synchronized(mutex) {
                writer.writeSize(res.size());
                
                for (int i = 0; i < res.size(); i++) {
                    res.get(i).marshall(writer);
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

    public java.util.Vector<DeviceSpec> getDeviceSpecs()
    {
        return DeviceManager.getAllDevices();
    }
}
