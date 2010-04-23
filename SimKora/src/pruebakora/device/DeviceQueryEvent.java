package pruebakora.device;

public class DeviceQueryEvent extends DeviceChangeEvent
{
    static final int TOPIC_EVENT_QUERY = 50;

    public DeviceQueryEvent(String deviceName, int type, Object newValue)
    {
        super(deviceName, type, newValue);
        type = TOPIC_EVENT_QUERY;
    }
}