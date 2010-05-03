package simkora.device;

import org.ugr.bluerose.events.*;

public class DeviceChangeEvent extends Event
{

    static final int TOPIC_EVENT_CHANGE = 49;
    private static final String TAG_NAME = "name",
            TAG_VALUE = "value";

    public DeviceChangeEvent(String deviceName, Value value)
    {
        Value name = new Value();

        name.setString(deviceName);

        setMember(TAG_NAME, name);
        setMember(TAG_VALUE, value);

        topic = TOPIC_EVENT_CHANGE;
    }

    public String getDeviceName()
    {
        Value vname = getMemberValue(TAG_NAME);
        if (vname != null) {
            return vname.getString();
        } else {
            return null;
        }
    }

    public Value getDeviceValue()
    {
        return getMemberValue(TAG_VALUE);
    }
}
