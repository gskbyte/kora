package org.gskbyte.kora.handling;

import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.customViews.koraButton.DeviceButton;
import org.gskbyte.kora.customViews.koraButton.DeviceButton.Attributes;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    private GridLayout mGrid;
    private Vector<KoraButton> mDeviceButtons;
    
    private LinearLayout mNavigationButtons;
    private KoraButton mBackButton, mNextButton;
    
    private boolean mShowPagingButtons;
    private KoraButton.Attributes mAttr;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeviceManager.init(this);
        
        setContentView(R.layout.device_selection_layout);
        
        //mNavigationButtons = (LinearLayout) findViewById(R.id.navigationButtons);
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mNavigationButtons = (LinearLayout) findViewById(R.id.navigationButtons);
        mBackButton = (KoraButton) findViewById(R.id.back);
        mNextButton = (KoraButton) findViewById(R.id.next);
        
        configureView();
        
        
        //mGrid.setClipToPadding(false);
        mGrid.setDimensions(3, 2);
        
        DeviceManager.connect();
        Attributes attr = new Attributes();
        attr.icon = DeviceRepresentation.ICON_ANIMATION;
        int nDevices = DeviceManager.getNumberOfDevices();
        for(int i=0; i<nDevices; ++i){
        	
        	DeviceButton b = new DeviceButton(this, attr, DeviceManager.getDeviceSystemName(i));
        	mGrid.addView(b);
        }
        
        /*
        
        w.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Toast.makeText(DeviceSelectionActivity.this, "HOLA", Toast.LENGTH_LONG).show();
            }});
        */
    }
    
    public void configureView()
    {
    	//UseProfile up = SettingsManager.
    	
    	// Propiedades de la rejilla
    	//mShowPagingButtons = ;

        //mNavigationButtons.setVisibility(View.GONE);
    }
    
}