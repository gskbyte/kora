package org.gskbyte.kora.handling;

import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.WelcomeActivity;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.deviceViews.DeviceSelectionButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceSelectionButton.Attributes;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.UseProfile.feedback;
import org.gskbyte.kora.settings.UseProfile.interaction;
import org.gskbyte.kora.settings.UseProfile.sound;
import org.gskbyte.kora.settings.UseProfile.visualization;
import org.gskbyte.kora.settingsActivities.SettingsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    private GridLayout mGrid;
    private Vector<KoraButton> mDeviceButtons;
    
    private GridLayout mNavigationButtons;
    private KoraButton mBackButton, mNextButton;
    
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
        

        // Conectar gestor de dispositivos
        DeviceManager.connect();
    }
    
    public void onStart()
    {
    	super.onStart();
    	
        // Configurar la vista y las propiedades de los botones
        configureView();
    	
    	// Rellenar la vista
        mGrid.removeAllViews();
        int nDevices = DeviceManager.getNumberOfDevices();
        for(int i=0; i<nDevices; ++i){
        	
        	DeviceSelectionButton b = new DeviceSelectionButton(this, 
        			(DeviceSelectionButton.Attributes)mAttr, 
        			DeviceManager.getDeviceSystemName(i));
        	mGrid.addView(b);
        }
    }
    
    public void configureView()
    {
    	UseProfile up = SettingsManager.getCurrentUseProfile();
    	mAttr = new DeviceSelectionButton.Attributes();
    	
    	// Opciones de vibración, orientación y demás (DESACTIVAR AL SALIR)
    	
    	// Propiedades de la rejilla
    	mShowPagingButtons = (up.paginationMode == UseProfile.interaction.pagination_buttons);
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
    		break;
    	case UseProfile.visualization.icon_black_white:
    		((DeviceSelectionButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_BLACK_WHITE;
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