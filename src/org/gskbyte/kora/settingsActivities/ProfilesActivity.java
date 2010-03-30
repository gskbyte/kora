package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
import org.gskbyte.kora.settings.Profile;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
	protected TextView mTitleText;
	protected TextView mCurrentProfileText;
	protected ImageView mCurrentProfileImage;
	protected Button mAddButton;

	protected SectionedListAdapter mAdapter;
    
	protected SettingsManager mSettings;
	protected Profile mCurrentProfile;

	protected String mSelectedProfileName;
	protected String mTitle;
	protected Drawable mPhoto;
	
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        mResources = getResources();

        mListView = (ListView) findViewById(R.id.listView);
        mTitleText = (TextView) findViewById(R.id.title);
        mCurrentProfileText = (TextView) findViewById(R.id.text);
        mCurrentProfileImage = (ImageView) findViewById(R.id.image);
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
	    updateCurrentUserView();
        updateListView();
	}
	
    public abstract void updateCurrentUserView();
	public abstract void updateListView();
	
    protected OnItemClickListener selectProfileListener;
    protected OnClickListener addProfileListener;

}
