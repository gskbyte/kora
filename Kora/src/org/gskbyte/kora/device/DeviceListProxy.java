package org.gskbyte.kora.device;

import java.util.Vector;

import org.ugr.bluerose.ByteStreamReader;
import org.ugr.bluerose.ByteStreamWriter;
import org.ugr.bluerose.ICommunicationDevice;
import org.ugr.bluerose.ObjectProxy;
import org.ugr.bluerose.messages.MessageHeader;

public class DeviceListProxy extends ObjectProxy
{
    public static final String TAG_NAME = "Kora",
                               TAG_CATEGORY = "Domotics";
    public static final String TAG_OPERATION = "getDeviceSpecs";
    
    public DeviceListProxy() throws Exception
    {
        super();
        identity.id_name = TAG_NAME;
        identity.category = TAG_CATEGORY;
        
        resolveInitialization(null, true, null);
    }
    
    public Vector<DeviceSpec> getDeviceSpecs()
    {
        currentMode = MessageHeader.TWOWAY_MODE;

        ByteStreamWriter writer = new ByteStreamWriter();

        int reqID = sendRequest(servantID, TAG_OPERATION, writer.toVector());
        java.util.Vector<Byte> result_bytes = this.receiveReply(reqID);
    
        ByteStreamReader reader = new ByteStreamReader(result_bytes);
        int n = reader.readSize();
    
        Vector<DeviceSpec> ret = new Vector<DeviceSpec>(n);
        for(int i=0; i<n; ++i){
            DeviceSpec ds = new DeviceSpec(reader);
            ret.add(ds);
        }
    
        return ret;
    }
    
    @Override
    public String getTypeID()
    {
        return TAG_CATEGORY+"::"+TAG_NAME;
    }
    

}