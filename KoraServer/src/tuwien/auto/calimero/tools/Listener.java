package tuwien.auto.calimero.tools;

import org.gskbyte.kora.server.util.Log;

import tuwien.auto.calimero.DataUnitBuilder;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.DatapointMap;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListenerEx;

final class Listener extends ProcessListenerEx
{
    private DatapointMap model = new DatapointMap();
    
    @Override
    public void groupReadRequest(final ProcessEvent e)
    {
        add(model.get(e.getDestination()), "read request", e);
    }

    @Override
    public void groupReadResponse(final ProcessEvent e)
    {
        add(model.get(e.getDestination()), "read response", e);
    }

    public void detached(final DetachEvent e)
    {}

    public void groupWrite(final ProcessEvent e)
    {
        add(model.get(e.getDestination()), "write", e);
    }

    private void add(final Datapoint dp, final String svc, final ProcessEvent e)
    {
        try {
            final String[] item = new String[] { e.getSourceAddr().toString(),
                e.getDestination().toString(), svc,
                DataUnitBuilder.toHex(e.getASDU(), " "),
                dp != null ? asString(e, dp.getMainNumber(), dp.getDPT()) : "n/a" };
        }
        catch (final KNXException e1) {
            Log.log("ProcessListenerEx", "KNXException\n"+e1.getMessage());
        }
        catch (final KNXIllegalArgumentException e1) {
            Log.log("ProcessListenerEx", "KNXIllegalArgumentException\n"+e1.getMessage());
        }
    }
}