package simkora.device;

import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.EventListener;
import org.ugr.bluerose.events.Value;
import simkora.SimKoraView;

public class DeviceChangeListener extends EventListener
{

    private static final String TAG = "DeviceChangeListener";

    public DeviceChangeListener()
    {
        super(DeviceChangeEvent.TOPIC_EVENT_CHANGE);
    }

    public void performAction(Event event)
    {
        String name = event.getMemberValue("name").getString();
        Value val = event.getMemberValue("value");

        System.out.println("EVENTO");

        if (name.equals("bombilla1")) {
            System.out.println("bombilla1 "+val.getBoolean());
            SimKoraView.getCurrent().setLight1(val.getBoolean(), false);
        } else if (name.equals("bombilla2")) {
            System.out.println("bombilla2 "+val.getFloat());
            SimKoraView.getCurrent().setLight2(val.getFloat(), false);
        } else if (name.equals("persiana1")) {
            System.out.println("persiana1 "+val.getFloat());
            SimKoraView.getCurrent().setSunblind(val.getFloat(), false);
        } else if (name.equals("puerta1")) {
            System.out.println("puerta1 "+val.getBoolean());
            SimKoraView.getCurrent().setDoor1(val.getBoolean(), false);
        } else if (name.equals("puerta2")) {
            System.out.println("puerta2 "+val.getBoolean());
            SimKoraView.getCurrent().setDoor2(val.getBoolean(), false);
        }
    }
}
