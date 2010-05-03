package org.gskbyte.kora.handling;

import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.customViews.KoraView;
import org.gskbyte.kora.customViews.deviceViews.DeviceSelectionButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceViewAttributes;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.profiles.ProfilesManager;
import org.gskbyte.kora.profiles.UseProfile;
import org.gskbyte.kora.profiles.ProfilesManager.SettingsException;
import org.gskbyte.kora.profilesActivities.useProfiles.SelectionActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    private GridLayout mGrid;
    
    private int mCurrentPage;
    
    private GridLayout mNavigationButtons;
    private KoraButton mPreviousButton, mNextButton;
    private KoraButton mGridNextButton;
    private int mIconReturnId = R.drawable.icon_back,
                mIconNextId = R.drawable.icon_next;
    
    private boolean mShowPagingButtons;
    private DeviceViewAttributes mAttr;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewManager.init(this);
        setContentView(R.layout.device_selection_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mNavigationButtons = (GridLayout) findViewById(R.id.navigationButtons);
        mPreviousButton = (KoraButton) findViewById(R.id.back);
        mNextButton = (KoraButton) findViewById(R.id.next);
        
        mPreviousButton.setOnClickListener(previousPageListener);
        mNextButton.setOnClickListener(nextPageListener);
        
        // Conectar gestor de dispositivos
        try {
            DeviceManager.connect();
            configureView();
            viewPage(0);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            
            String errorString = getString(R.string.address) + ": " + DeviceManager.getServerAddress() + "\n" +
                                 getString(R.string.port)    + ": " + DeviceManager.getServerPort();
            
            builder.setTitle(R.string.connectionError)
            .setMessage(errorString)
            .setIcon(R.drawable.icon_error)
            .setCancelable(false)
            .setNeutralButton(R.string.return_,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
            mNavigationButtons.setVisibility(View.GONE);
            builder.show();
        }
    }
    
    public void onStart()
    {
    	super.onStart();
    	
    	//int nbuttons = mGrid.getChildCount();
    	//for(int i=0; i<nbuttons; ++i)
    	//    ((KoraButton)mGrid.getChildAt(i)).deselect();
    }
    
    public void configureView()
    {
        mAttr = ViewManager.getAttributes();
        UseProfile up = ProfilesManager.getCurrentUseProfile();
        mGrid.setDimensions(up.rows, up.columns);
        switch(up.margin){
        case UseProfile.visualization.margin_large:
            mGrid.setMargin(20);
            break;
        case UseProfile.visualization.margin_medium:
            mGrid.setMargin(10);
            break;
        case UseProfile.visualization.margin_small:
        default:
            mGrid.setMargin(5);
            break;
        }
        
        switch(mAttr.icon){
        case  DeviceRepresentation.ICON_HIGH_CONTRAST:
            mIconReturnId = R.drawable.icon_back_hc;
            mIconNextId = R.drawable.icon_next_hc;
            break;
        case DeviceRepresentation.ICON_BLACK_WHITE:
            mIconReturnId = R.drawable.icon_back_bw;
            mIconNextId = R.drawable.icon_next_bw;
            break;
        default: // para resto de opciones
            mIconReturnId = R.drawable.icon_back;
            mIconNextId = R.drawable.icon_next;
            break;
        }
        
        mShowPagingButtons = (up.paginationMode == UseProfile.interaction.pagination_standard);
        int nDevices = DeviceManager.getNumberOfDevices();
        if(mShowPagingButtons && nDevices>mGrid.getNRows()*mGrid.getNColumns()){
            mNavigationButtons.setVisibility(View.VISIBLE);
            
            mPreviousButton.setIcon(mIconReturnId);
            mPreviousButton.setAttributes(ViewManager.getAttributes(ViewManager.COLOR_INDEX_PREVIOUS, true));
            mNextButton.setIcon(mIconNextId);
            mNextButton.setAttributes(ViewManager.getAttributes(ViewManager.COLOR_INDEX_NEXT, true));
        } else {
            mNavigationButtons.setVisibility(View.GONE);
        }
    }
    
    protected Vector<DeviceSelectionButton> getButtonsForDevices(int initialIndex, int howMany)
    {
        Vector<DeviceSelectionButton> ret = new Vector<DeviceSelectionButton>();
        
        for(int i=initialIndex; i<initialIndex+howMany; ++i){
            DeviceSelectionButton b = new DeviceSelectionButton(this, 
                    ViewManager.getAttributes(i), 
                    DeviceManager.getDeviceSystemName(i));
            ret.add(b);
        }
        
        return ret;
    }
    
    protected int truncateTop(float n)
    {
        int in = (int)n;
        
        if(in < n)
            return in+1;
        else
            return in;
    }
    
    protected void viewPage(int page)
    {
        int nDevices = DeviceManager.getNumberOfDevices();
        int gridSize = mGrid.getNRows()*mGrid.getNColumns();
        Vector<DeviceSelectionButton> btns;
        int nDeviceButtons = mShowPagingButtons ? gridSize : gridSize-1;
        int nPages = truncateTop((float)nDevices / (float)nDeviceButtons);
        if(nDevices>gridSize){
            
            if(page<0)
                page = nPages-1;
            else if(page>=nPages)
                page = 0;
            
            int initialIndex = page*nDeviceButtons;
            int howMany = (nDevices-initialIndex>=nDeviceButtons) ? nDeviceButtons : nDevices-initialIndex;
            btns = getButtonsForDevices(initialIndex, howMany);
        } else {
            btns = getButtonsForDevices(0, nDevices);
        }
        
        // Rellenar la vista
        mGrid.removeAllViews();
        for(DeviceSelectionButton b : btns)
            mGrid.addView(b);
        

        // Añadir botón de paginación si es necesario
        if(!mShowPagingButtons){
            if(nPages>1){
                for(int i=0; i<gridSize-btns.size()-1; ++i)
                    mGrid.addView(new View(this));
                if (mGridNextButton == null){
                    String moreString = getResources().getString(R.string.more);
                    mGridNextButton = new KoraButton(this, moreString,
                            R.drawable.action_next,
                            ViewManager.getAttributes(ViewManager.COLOR_INDEX_NEXT));
                    mGridNextButton.setOnClickListener(nextPageListener);
                    mGridNextButton.setIcon(mIconNextId);
                }
                mGridNextButton.deselect();
                mGrid.addView(mGridNextButton);
            }
        } else {
            mPreviousButton.deselect();
            mNextButton.deselect();
        }
        mCurrentPage = page;
    }

    View.OnClickListener previousPageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPage(mCurrentPage-1);
            }
        };
    
    View.OnClickListener nextPageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPage(mCurrentPage+1);
            }
        };
}