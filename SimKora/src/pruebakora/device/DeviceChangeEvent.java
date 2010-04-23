package pruebakora.device;

import org.ugr.bluerose.events.*;

public class DeviceChangeEvent extends Event
{
    static final int TOPIC_EVENT_CHANGE = 49;

    private static final String TAG_NAME = "name",
                                TAG_VALUE = "value";

    public DeviceChangeEvent(String deviceName, int type, Object newValue)
    {
        Value name = new Value(),
              value = new Value();

        name.setString(deviceName);

        switch(type)
        {
        case DeviceSpec.VALUE_BOOLEAN:
            value.setBoolean((Boolean)newValue);
            break;
        case DeviceSpec.VALUE_FLOAT:
            value.setFloat((Float)newValue);
            break;
        case DeviceSpec.VALUE_INTEGER:
            value.setInteger((Integer)newValue);
            break;
        case DeviceSpec.VALUE_LONG:
            value.setLong((Long)newValue);
            break;
        case DeviceSpec.VALUE_STRING:
        default:
            value.setString(newValue.toString());
            break;
        }

        setMember(TAG_NAME, name);
        setMember(TAG_VALUE, value);

        type = TOPIC_EVENT_CHANGE;
    }

    public String getDeviceName()
    {
        Value vname  = getMemberValue(TAG_NAME);
        if(vname != null){
                return vname.getString();
        } else {
                return null;
        }
    }

    public Object getDeviceValue()
    {
        Value v = getMemberValue(TAG_VALUE);
        if(v != null){
            switch(v.type){
            case Value.BOOLEAN_TYPE:
                    return v.getBoolean();
            case Value.FLOAT_TYPE:
                    return v.getFloat();
            case Value.INTEGER_TYPE:
                    return v.getInteger();
            case Value.LONG_TYPE:
                    return v.getLong();
            case Value.STRING_TYPE:
                    return v.getString();
            default:
                    return null;
            }
        } else {
            return null;
        }
    }
}
