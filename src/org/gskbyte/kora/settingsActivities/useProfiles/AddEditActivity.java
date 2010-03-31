package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AddEditActivity extends Activity
{
    private static final String TAG = "UseProfileAddEditActivity";
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_add_edit);        
    }

}
