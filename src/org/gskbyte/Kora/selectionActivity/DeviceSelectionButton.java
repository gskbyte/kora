package org.gskbyte.Kora.selectionActivity;

import org.gskbyte.Kora.R;
import org.gskbyte.Kora.Settings.Device;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceSelectionButton extends FrameLayout
{
	private ImageView mImageView;
	private TextView mTextView;
	
	public DeviceSelectionButton(Context context, Device device)
	{
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.device_selection_item, this, true);
		mImageView = (ImageView) findViewById(R.id.deviceIcon);
		mTextView = (TextView) findViewById(R.id.deviceName);
		
		//mImageView.setImageDrawable(device.getIcon(Device.ICON_NORMAL));
		// Cambiar por obtener el nombre TRADUCIBLE
		mTextView.setText(device.getName());
	}
	
	
}
