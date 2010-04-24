package org.gskbyte.kora.handling;

import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.deviceViews.DeviceSelectionButton;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    private GridLayout mGrid;
    
    private int mCurrentPage;
    
    private GridLayout mNavigationButtons;
    private KoraButton mBackButton, mNextButton;
    private KoraButton mGridNextButton;
    private int mIconBackId = R.drawable.icon_back,
                mIconNextId = R.drawable.icon_next;
    
    private boolean mShowPagingButtons;
    private KoraButton.Attributes mAttr;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.device_selection_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mNavigationButtons = (GridLayout) findViewById(R.id.navigationButtons);
        mBackButton = (KoraButton) findViewById(R.id.back);
        mNextButton = (KoraButton) findViewById(R.id.next);
        
        mBackButton.setOnClickListener(previousPageListener);
        mNextButton.setOnClickListener(nextPageListener);
        
        // Conectar gestor de dispositivos
        DeviceManager.connect();
        configureView();
        
        viewPage(0);
    }
    
    public void onStart()
    {
    	super.onStart();
    	
    	int nbuttons = mGrid.getChildCount();
    	for(int i=0; i<nbuttons; ++i)
    	    ((KoraButton)mGrid.getChildAt(i)).deselect();
    }
    
    public void configureView()
    {
    	UseProfile up = SettingsManager.getCurrentUseProfile();
    	mAttr = new DeviceSelectionButton.Attributes();
    	
    	// Opciones de vibración, orientación y demás (DESACTIVAR AL SALIR)
    	
    	// Propiedades de la rejilla
    	mShowPagingButtons = (up.paginationMode == UseProfile.interaction.pagination_standard);
        if(mShowPagingButtons)
        	mNavigationButtons.setVisibility(View.VISIBLE);
        else
        	mNavigationButtons.setVisibility(View.GONE);
        	
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
    	
    	
    	// Propiedades de botones
			// viewMode
			// backColor
    	
    	// showText
    	mAttr.showText = up.showText;
    	
		// fontSize
    	switch(up.textSize){
    	case UseProfile.visualization.text_size_large:
    		mAttr.textMaxSize = KoraButton.Attributes.TEXT_LARGE;
    		break;
    	case UseProfile.visualization.text_size_medium:
    		mAttr.textMaxSize = KoraButton.Attributes.TEXT_MEDIUM;
    		break;
    	case UseProfile.visualization.text_size_small:
    		mAttr.textMaxSize = KoraButton.Attributes.TEXT_SMALL;
		default:
    		break;
    	}
			// typography
			// typographyCaps
    	
		// textColor
	    mAttr.textColor = up.textColor;
	    	
		// iconMode
    	switch(up.iconMode){
    	case UseProfile.visualization.icon_high_contrast:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_HIGH_CONTRAST;
    		mIconBackId = R.drawable.icon_back_hc;
    		mIconNextId = R.drawable.icon_next_hc;
    		break;
    	case UseProfile.visualization.icon_black_white:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_BLACK_WHITE;
            mIconBackId = R.drawable.icon_back_bw;
            mIconNextId = R.drawable.icon_next_bw;
    		break;
    	case UseProfile.visualization.icon_photo:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_PHOTO;
    		break;
    	case UseProfile.visualization.icon_animation:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_ANIMATION;
    		break;
    	case UseProfile.visualization.icon_pictogram:
		default:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_DEFAULT;
    		break;
    	}
    	mBackButton.setIcon(mIconBackId);
        mNextButton.setIcon(mIconNextId);
	    	
    		// customImage
    	
    	
    	
    	mAttr.vibrate = up.vibration;
        	// confirmation
    		// confirmationTimeMillis
    		// contentHighlight
    		// borderHighlight
    	
    	/* TTS
    	 * http://android-developers.blogspot.com/2009/09/introduction-to-text-to-speech-in.html
    	 * */
    	
    }
    
    protected Vector<DeviceSelectionButton> getButtonsForDevices(int initialIndex, int howMany)
    {
        Vector<DeviceSelectionButton> ret = new Vector<DeviceSelectionButton>();
        
        for(int i=initialIndex; i<initialIndex+howMany; ++i){
            DeviceSelectionButton b = new DeviceSelectionButton(this, 
                    (DeviceSelectionButton.Attributes)mAttr, 
                    DeviceManager.getDeviceSystemName(i));
            ret.add(b);
        }
        
        return ret;
    }
    
    protected void viewPage(int page)
    {
        int nDevices = DeviceManager.getNumberOfDevices();
        int gridSize = mGrid.getNRows()*mGrid.getNColumns();
        Vector<DeviceSelectionButton> btns;
        if(nDevices>gridSize){
            int nDeviceButtons = mShowPagingButtons ? gridSize : gridSize-1;
            int nPages = nDevices / nDeviceButtons + 1;
            
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
            for(int i=0; i<gridSize-btns.size()-1; ++i)
                mGrid.addView(new View(this));
            if (mGridNextButton == null){
                String moreString = getResources().getString(R.string.more);
                mGridNextButton = new KoraButton(this, moreString, R.drawable.action_next);
                mGridNextButton.setOnClickListener(nextPageListener);
                mGridNextButton.setIcon(mIconNextId);
            }
            mGridNextButton.deselect();
            mGrid.addView(mGridNextButton);
        } else {
            mBackButton.deselect();
            mNextButton.deselect();
        }
        mCurrentPage = page;
    }

    View.OnClickListener previousPageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPage(mCurrentPage+1);
            }
        };
    
    View.OnClickListener nextPageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPage(mCurrentPage-1);
            }
        };
    
	// Con un asterisco marco las propiedades implementadas
    // * = TOTALMENTE IMPLEMENTADO
    // - = A MEDIAS

    
    /*
    // Interaction settings
    public int mainInteraction = interaction.touch_mode;
    public int touchMode = interaction.press_and_drag;
    public int scanMode = interaction.simple_scan;
    public int scanTimeMillis = 2500;
    - public int paginationMode = interaction.pagination_standard; (solo cambio botón, implementar automático)
    public int voiceInteraction = interaction.no_voice;
    
    // Visualization settings
    public int viewMode = visualization.view_standard;
    public int backgroundColor = 0xFF000000;
    * public int rows = 2;
    * public int columns = 2;
    * public int margin = visualization.margin_small;
    public int orientations = visualization.orientation_both;
    * public boolean showText = true;
    * public int textSize = visualization.text_size_small;
    public int typography = visualization.font_sans;
    public boolean typographyCaps = false;
    * public int textColor = 0xFF000000;
    * public int iconMode = visualization.icon_pictogram;
    public boolean customImage = false;
    
    // Feedback settings
    * public boolean vibration = false;
    public boolean confirmation = false;
    public int confirmationTimeMillis = 3000;
    public int contentHighlight = feedback.content_highlight_standard;
    public boolean borderHighlight = false;
    
    // Sound settings
    public int soundMode = sound.no_sounds;
    public boolean soundOnSelection = false;
    public boolean soundOnAction = false;
    public int voiceMode = sound.voice_default;*/
}