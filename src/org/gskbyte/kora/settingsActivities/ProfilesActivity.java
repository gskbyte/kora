package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
import org.gskbyte.kora.settings.Profile;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class ProfilesActivity extends Activity
{
	private static final String TAG = "ProfilesActivity";
	
	/* Request codes */
    public static final int ADD_REQUEST = 0;
    public static final int CHOOSE_REQUEST = 1;
    public static final int COPY_REQUEST = 2;
    public static final int EDIT_REQUEST = 3;
    public static final int DELETE_REQUEST = 4;
	
    /* Dialog IDs */
    public static final int COPY_DIALOG_ID = 1;
    public static final int EDIT_DIALOG_ID = 2;
    public static final int SELECT_DIALOG_ID = 3;
    public static final int CONFIRM_DELETE_DIALOG_ID = 4;
    
	protected Resources mResources;
	
	protected ListView mListView;
	protected TextView mCurrentUserText, mCurrentUseProfileText,
	                   mCurrentDeviceProfileText;
	protected ImageView mCurrentUserPhoto;
	protected Button mAddButton;

	protected SectionedListAdapter mAdapter;
    
	protected SettingsManager mSettings;
    protected User mCurrentUser;

	protected String mSelectedProfileName;
	protected String mTitle;
	protected Drawable mPhoto;
	
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        mResources = getResources();

        mListView = (ListView) findViewById(R.id.listView);
        mCurrentUserText = (TextView) findViewById(R.id.userName);
        mCurrentUseProfileText = (TextView) findViewById(R.id.useProfileName);
        mCurrentDeviceProfileText = (TextView) findViewById(R.id.deviceProfileName);
        mCurrentUserPhoto = (ImageView) findViewById(R.id.userPhoto);
        mAddButton = (Button) findViewById(R.id.addButton);

        /* Iniciar adaptadores de listas */
        mAdapter=new SectionedListAdapter(this);
        mListView.setAdapter(mAdapter);
	}
	
	public void onStart()
	{
	    super.onStart();
        
        /* Asociar eventos */
	    mListView.setOnItemClickListener(selectProfileListener);
	    mAddButton.setOnClickListener(addProfileListener);
	}
	
	public void onResume()
	{
	    super.onResume();
	    
        /* Actualizar vista */
	    updateCurrentProfileView();
        updateListView();
	}
	
    public abstract void updateCurrentProfileView();
	public abstract void updateListView();
	
    protected OnItemClickListener selectProfileListener;
    protected OnClickListener addProfileListener;

}
