package org.gskbyte.kora;

import org.gskbyte.kora.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class InfoDialogActivity extends Activity
{
	private static final String TAG = "InfoDialog";
	
	public void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
	    setContentView(R.layout.info_dialog);
	    setTitle(getString(R.string.appName)+" "+getString(R.string.versionNumber));
	    getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
	            R.drawable.icon_info);
    }
}
