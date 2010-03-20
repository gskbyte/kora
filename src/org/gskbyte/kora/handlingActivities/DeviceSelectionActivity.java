package org.gskbyte.kora.handlingActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.R.drawable;
import org.gskbyte.kora.R.id;
import org.gskbyte.kora.R.layout;
import org.gskbyte.kora.customViews.gridLayout.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    GridLayout mGrid;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.device_selection_layout);
        
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mGrid.setClipToPadding(false);
        mGrid.setNColumns(3);
        mGrid.setNRows(3);
        
        
        KoraButton w = new KoraButton(this, "Heater", R.drawable.icon_device_heater_128, null),
                   w2 = new KoraButton(this, "Light", R.drawable.icon_device_light_128, null),
                   w3 = new KoraButton(this, "Music", R.drawable.icon_device_music_128, null),
                   w4 = new KoraButton(this, "Sunblind", R.drawable.icon_device_sunblind_128, null),
                   w5 = new KoraButton(this, "Light", R.drawable.icon_device_light_128, null);
        
        mGrid.addView(w);
        mGrid.addView(w2);
        mGrid.addView(w3);
        mGrid.addView(w4);
        mGrid.addView(w5);
        
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new OnClickListener(){
	        @Override
	        public void onClick(View v)
	        {
	            mGrid.setNColumns(2);
	            mGrid.setNRows(2);
	        }
        });
    }
    
    
}