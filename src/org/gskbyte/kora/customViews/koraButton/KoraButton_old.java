package org.gskbyte.kora.customViews.koraButton;

import org.gskbyte.kora.R;
import org.gskbyte.kora.device.Device;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class KoraButton_old extends FrameLayout
{
	private ImageView mImageView;
	private TextView mTextView;
	
	public KoraButton_old(Context context, Device device)
	{
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.device_selection_item, this, true);
		mImageView = (ImageView) findViewById(R.id.deviceIcon);
		mTextView = (TextView) findViewById(R.id.deviceName);
		
		mImageView.setImageDrawable(device.getIcon(Device.ICON_NORMAL));
		// Cambiar por obtener el nombre TRADUCIBLE
		mTextView.setText(device.getName());
	}
	
	
}
