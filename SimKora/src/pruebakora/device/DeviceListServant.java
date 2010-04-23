/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebakora.device;

import java.util.Vector;
import org.ugr.bluerose.ByteStreamWriter;
import org.ugr.bluerose.DevicePool;
import org.ugr.bluerose.MethodResult;
import org.ugr.bluerose.events.Value;

/**
 *
 * @author jose
 */
public class DeviceListServant extends org.ugr.bluerose.ObjectServant
{

    public DeviceListServant()
    {
        super();
        identity.id_name = "Kora";
        identity.category = "Domotics";

        DevicePool.registerObjectServant(this);
    }

    @Override
    public void finalize() throws Throwable
    {
        DevicePool.unregisterObjectServant(this);

        super.finalize();
    }

    @Override
    public MethodResult runMethod(String userID, String method, Vector<Byte> args)
    {
	ByteStreamWriter writer = new ByteStreamWriter();

        MethodResult result = new MethodResult();

        if (method.equals("getDevices"))
        {
            java.util.Vector<DeviceSpec> res = getDevices();

            result.status = org.ugr.bluerose.messages.MessageHeader.SUCCESS_STATUS;

            writer.writeSize(res.size());
            for (int i=0; i<res.size(); i++)
            {
                res.get(i).marshall(writer);
            }

            result.result = writer.toVector();

            return result;
        }

        result.status = org.ugr.bluerose.messages.MessageHeader.OPERATION_NOT_EXIST_STATUS;
        return result;
    }

    public java.util.Vector<DeviceSpec> getDevices()
    {
        Vector<DeviceSpec> specs = new Vector<DeviceSpec>();

    	Value s1min = new Value(),
    	      s1max = new Value();
    	s1min.setBoolean(false);
    	s1max.setBoolean(true);
    	DeviceSpec s1 = new DeviceSpec("bombilla1",
    			"Luz del pasillo",
    			"simpleLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.BOOLEAN_TYPE,
    			s1min,
    			s1max);

    	Value s2min = new Value(),
              s2max = new Value();
        s2min.setInteger(10);
        s2max.setInteger(0);
    	DeviceSpec s2 = new DeviceSpec("tableLight",
    			"Luz de la mesita",
    			"adjustableLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.INTEGER_TYPE,
    			s2min,
    			s2max);

    	Value s3min = new Value(),
              s3max = new Value();
        s3min.setInteger(10);
        s3max.setInteger(0);
    	DeviceSpec s3 = new DeviceSpec("otro",
    			"Cacharro",
    			"other",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.INTEGER_TYPE,
    			s3min,
    			s3max);

    	specs.add(s1);
    	specs.add(s3);
    	specs.add(s2);

        return null;
    }

}
