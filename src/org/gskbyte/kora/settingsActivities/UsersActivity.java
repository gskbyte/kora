package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class UsersActivity extends Activity
{
    private static final String TAG = "UsersActivity";
    
    private Resources mResources;
    
    private ListView mListView;
    private TextView mTitle;
    private TextView mCurrentUserNameTextView;
    private ImageView mCurrentUserImageView;
    private Button mAddUserButton;

    private SectionedListAdapter mAdapter;
    
    private SettingsManager mSettings;
    private User mCurrentUser;

    private String mSelectedUserName;
    
    /* Dialog IDs */
    private static final int ADD_USER_DIALOG_ID = 0;
    private static final int EDIT_USER_DIALOG_ID = 1;
    private static final int SELECT_USER_DIALOG_ID = 2;
    private static final int CONFIRM_DELETE_USER_DIALOG_ID = 3;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        mResources = getResources();

        /* Cargar elementos de la vista */
        mListView = (ListView) findViewById(R.id.listView);
        mTitle = (TextView) findViewById(R.id.title);
        mCurrentUserNameTextView = (TextView) findViewById(R.id.text);
        mCurrentUserImageView = (ImageView) findViewById(R.id.image);
        mAddUserButton = (Button) findViewById(R.id.addButton);
       
        /* Iniciar manejadores */
        try {
            mSettings = SettingsManager.getInstance();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        mCurrentUser = mSettings.getCurrentUser();
        
        /* Iniciar adaptadores de listas */
        //mCustomUsersAdapter = new DetailedListViewAdapter(this, R.layout.detailedlistview);
        //mDefaultUsersAdapter = new DetailedListViewAdapter(this, R.layout.detailedlistview);
        mAdapter=new SectionedListAdapter(this/*, R.layout.detailedlistview*/);
        
        /* Ajustar vista */

        updateCurrentUserView();
        updateUsersList(true);
        mListView.setAdapter(mAdapter);
        
        
        /* Asociar eventos */
        mListView.setOnItemClickListener(selectUserListener);
        mAddUserButton.setOnClickListener(addUserListener);
    }
    
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog=null;
        User u;
        switch(id)
        {
        case ADD_USER_DIALOG_ID:
            dialog = new AddEditUserDialog(this, null);
            break;
        case EDIT_USER_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedUserName);
                dialog = new AddEditUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedUserName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
            break;
        case SELECT_USER_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedUserName);
                dialog = new SelectUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedUserName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
            break;
        case CONFIRM_DELETE_USER_DIALOG_ID:
            dialog = createConfirmDeleteUserDialog();
            break;
        default:
            dialog = null;
        }

        return dialog;
    }
    
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        User u;
        switch(id)
        {
        case ADD_USER_DIALOG_ID:
            ((AddEditUserDialog)dialog).setUser(null);
            break;
        case EDIT_USER_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedUserName);
                ((AddEditUserDialog)dialog).setUser(u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                        "User not found: "+mSelectedUserName+". Reset app.\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            break;
        case SELECT_USER_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedUserName);
                ((SelectUserDialog)dialog).setUser(u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                        "User not found: "+mSelectedUserName+". Reset app.\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            break;
        case CONFIRM_DELETE_USER_DIALOG_ID:
            break;
        default:
            dialog = null;
        }
    }
    
    protected void updateCurrentUserView()
    {
        mCurrentUserNameTextView.setText(mCurrentUser.getName());
        mCurrentUserImageView.setImageDrawable(mCurrentUser.getPhoto());
    }

    protected void updateUsersList(boolean first_time)
    {
        // Usuarios personalizados
        Collection<User> customUsers = (Collection) mSettings.getCustomUsers();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(User u : customUsers)
            customs.add(new DetailedViewModel(
                    u.getName(),
                    u.getName(),
                    u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                    u.getPhoto()));
        
        if(first_time){
            // Usuarios por defecto
            Collection<User> defaultUsers = (Collection) mSettings.getDefaultUsers();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(User u : defaultUsers)
                defaults.add(new DetailedViewModel(
                        u.getName(),
                        u.getName(),
                        u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                        u.getPhoto()));
            
            if(customs.size()>0)
                mAdapter.addSection(mResources.getString(R.string.customs), customs);
            mAdapter.addSection(mResources.getString(R.string.defaults), defaults);
        } else {
            mAdapter.removeSection(mResources.getString(R.string.customs));
            if(customs.size()>0)
                mAdapter.addSection(0, mResources.getString(R.string.customs), customs);
            mAdapter.notifyDataSetChanged();
        }
        
        mAdapter.notifyDataSetChanged();
    }
    
    public void chooseCurrentUser()
    {
        try {
            mSettings.setCurrentUser(mSelectedUserName);
            mCurrentUser = mSettings.getCurrentUser();
            updateCurrentUserView();
        } catch (SettingsException e) {
            Toast.makeText(this, 
                "User not found: "+mSelectedUserName+". Reset app.\n"+e.getMessage(),
                Toast.LENGTH_LONG).show();
        }
    }

    public void addUser(User u)
    {
        try{
            mSettings.addUser(u);
            Toast.makeText(this, 
                    "Usuario añadido: "+u.getName(), Toast.LENGTH_SHORT).show();
            updateUsersList(false);
        } catch (SettingsManager.SettingsException e){
            Toast.makeText(this, 
                    "Fallo al añadir el usuario "+u.getName(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void showEditUserDialog()
    {
        showDialog(EDIT_USER_DIALOG_ID);
    }
    
    public void editUser(String previous_name, User u)
    {
        try{
            mSettings.editUser(previous_name, u);
            Toast.makeText(this, 
                    "Edición correcta: "+previous_name+" -> "+u.getName(), Toast.LENGTH_SHORT).show();
            if(previous_name.equals(mCurrentUser.getName())){
                mCurrentUser = u;
                updateCurrentUserView();
            }
            updateUsersList(false);
        }catch (SettingsManager.SettingsException e){
            Toast.makeText(this,
                    "Edición incorrecta: "+previous_name+" -X> "+u.getName(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void showDeleteUserDialog()
    {
        showDialog(CONFIRM_DELETE_USER_DIALOG_ID);
    }
    
    public void deleteUser()
    {
        boolean ok = !mCurrentUser.getName().equals(mSelectedUserName);
        if(ok){
            try{
                mSettings.removeUser(mSelectedUserName);
                Toast.makeText(this, 
                        "Usuario borrado con éxito: "+mSelectedUserName, Toast.LENGTH_SHORT).show();
                updateUsersList(false);
            }catch (SettingsManager.SettingsException e){
                Toast.makeText(this,
                        "Se produjo un error al borrar el usuario: "+mSelectedUserName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, 
                    "Seleccione otro usuario actual antes de borrarlo.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Dialog createConfirmDeleteUserDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mResources.getString(R.string.userDeletionQuestion)+ " " + mSelectedUserName + "?")
               //.setIcon()
               .setCancelable(false)
               .setPositiveButton(mResources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        deleteUser();
                   }
               })
               .setNegativeButton(mResources.getString(R.string.no), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        return builder.create();
    }
    
    private OnItemClickListener selectUserListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id)
        {
            mAdapter.setSelected(position);
            DetailedViewModel selected_model = 
                (DetailedViewModel) mAdapter.getItem(position);
            mSelectedUserName = selected_model.tag();
            
            showDialog(SELECT_USER_DIALOG_ID);
        }

    };

    private OnClickListener addUserListener = new OnClickListener() {
        public void onClick(View v) {
            showDialog(ADD_USER_DIALOG_ID);
        }
    };
}
