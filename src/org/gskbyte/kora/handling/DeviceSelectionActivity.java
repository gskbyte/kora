package org.gskbyte.kora.handling;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.customViews.koraButton.KoraButton.Attributes;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    GridLayout mGrid;
    LinearLayout mNavigationButtons;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.device_selection_layout);
        
        //mNavigationButtons = (LinearLayout) findViewById(R.id.navigationButtons);
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        
        //mNavigationButtons.setVisibility(View.GONE);
        
        mGrid.setClipToPadding(false);
        mGrid.setDimensions(3, 2);
        
        KoraButton.resetMinTextSize();
        KoraButton w  = new KoraButton(this, "Calentador", R.drawable.icon_device_heater_128),
                   w2 = new KoraButton(this, "Luz salón", R.drawable.icon_device_light_128),
                   w3 = new KoraButton(this, "Música", R.drawable.icon_device_music_128),
                   w4 = new KoraButton(this, "Persiana", R.drawable.icon_device_sunblind_128),
                   w5 = new KoraButton(this, "Luz", R.drawable.icon_device_light_128),
                   w6 = new KoraButton(this, "Más", R.drawable.icon_more);
        
        KoraButton.Attributes attr;
        
        attr = w.getAttributes();
        attr.backgroundColors[Attributes.INDEX_FOCUSED] = Color.rgb(15, 100, 15);
        w.setAttributes(attr);
        
        attr = w2.getAttributes();
        //attr.textMaxSize = Attributes.TEXT_BIG;
        attr.backgroundColors[Attributes.INDEX_FOCUSED] = Color.rgb(147, 166, 132);
        w2.setAttributes(attr);
        
        mGrid.addView(w);
        mGrid.addView(w2);
        mGrid.addView(w3);
        mGrid.addView(w4);
        mGrid.addView(w5);
        mGrid.addView(w6);
        
        w.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Toast.makeText(DeviceSelectionActivity.this, "HOLA", Toast.LENGTH_LONG).show();
            }});
        /*
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
        */
    }
    
    
}