package org.gskbyte.Kora.selectionActivity;

import java.util.Vector;

import org.gskbyte.Kora.Settings.Device;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        DeviceSelectionButton view;
        if (convertView == null) {
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
