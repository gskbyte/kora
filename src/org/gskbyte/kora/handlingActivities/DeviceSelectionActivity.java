package org.gskbyte.kora.handlingActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.R.drawable;
import org.gskbyte.kora.R.id;
import org.gskbyte.kora.R.layout;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

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
        mGrid.setDimensions(3, 2);
        
        
        KoraButton w = new KoraButton(this, "Heater", R.drawable.icon_device_heater_128, null),
                   w2 = new KoraButton(this, "Light", R.drawable.icon_device_light_128, null),
                   w3 = new KoraButton(this, "Music", R.drawable.icon_device_music_128, null),
                   w4 = new KoraButton(this, "Sunblind", R.drawable.icon_device_sunblind_128, null),
                   w5 = new KoraButton(this, "Light", R.drawable.icon_device_light_128, null);
        
        KoraButton.Attributes attr;
        
        attr = w.getAttributes();
        attr.borderScale = (float) 1.2;
        attr.backgroundColors[KoraButton.STATE_FOCUSED] = Color.rgb(15, 100, 15);
        w.setAttributes(attr);
        
        attr = w2.getAttributes();
        attr.textScale = (float) 1.5;
        attr.backgroundColors[KoraButton.STATE_FOCUSED] = Color.rgb(147, 166, 132);
        w2.setAttributes(attr);
        
        mGrid.addView(w);
        mGrid.addView(w2);
        mGrid.addView(w3);
        mGrid.addView(w4);
        mGrid.addView(w5);
        
        w.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Toast.makeText(DeviceSelectionActivity.this, "HOLA", Toast.LENGTH_LONG).show();
            }});
        
        Button lessRows = (Button) findViewById(R.id.lessRows);
        lessRows.setOnClickListener(new OnClickListener(){
	        @Override
	        public void onClick(View v)
	        {
	            mGrid.setDimensions(mGrid.getNRows()-1, mGrid.getNColumns());
	        }
        });
        
        Button moreRows = (Button) findViewById(R.id.moreRows);
        moreRows.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mGrid.setDimensions(mGrid.getNRows()+1, mGrid.getNColumns());
            }
        });
        
        Button lessColumns = (Button) findViewById(R.id.lessColumns);
        lessColumns.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mGrid.setDimensions(mGrid.getNRows(), mGrid.getNColumns()-1);
            }
        });
        
        Button moreColumns = (Button) findViewById(R.id.moreColumns);
        moreColumns.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mGrid.setDimensions(mGrid.getNRows(), mGrid.getNColumns()+1);
            }
        });
    }
    
    
}