package org.gskbyte.kora.customViews;

import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.device.Device;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DeviceAdapter extends BaseAdapter
{
    public DeviceAdapter(Context c)
    {
        mContext = c;
    }

    public void addDevice(Device d)
    {
    	devices.add(d);
    }
    
    public int getCount()
    {
        return devices.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        //View view;
        DeviceSelectionButton view;
        if (convertView == null) {
            //view = new DeviceSelectionWidget(mContext, R.drawable.icon_device_heater_128, "Heater");
            view = new DeviceSelectionButton(mContext, devices.get(position));
            //view.setLayoutParams(new GridView.LayoutParams(45, 45));
            //view.setAdjustViewBounds(false);
            //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //view.setPadding(8, 8, 8, 8);
        } else {
            view = (DeviceSelectionButton) convertView;
        }

        return view;
    }

    private Context mContext;
    private Vector<Device> devices = new Vector<Device>();
}
