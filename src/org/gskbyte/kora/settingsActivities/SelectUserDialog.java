package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class SelectUserDialog extends AlertDialog
{
    Resources resources;
    UsersActivity activity;
    
    Button chooseButton, editButton, deleteButton;
    
    public interface Listener
    {
        public void chooseUser();
        public void editUser();
        public void deleteUser();
    }
    
    public SelectUserDialog(Context context, User user)
    {
        super(context);
        resources = context.getResources();
        activity = (UsersActivity) context;
        
        View v = View.inflate(context, R.layout.select_user_dialog, null);
        chooseButton = (Button) v.findViewById(R.id.chooseButton);
        editButton = (Button) v.findViewById(R.id.editButton);
        deleteButton = (Button) v.findViewById(R.id.deleteButton);
        
        // Igualar ancho de los botones
        /*int chooseButtonWidth = chooseButton.getWidth(),
            editButtonWidth = editButton.getWidth(),
            deleteButtonWidth = deleteButton.getWidth();

        
        int maximum = Math.max(chooseButtonWidth, editButtonWidth);
        maximum = Math.max(maximum, deleteButtonWidth);
        
        
        chooseButton.setMinimumWidth(maximum);
        editButton.setMinimumWidth(maximum);
        deleteButton.setMinimumWidth(maximum);
        
        chooseButton.setWidth(maximum);
        editButton.setWidth(maximum);
        deleteButton.setWidth(maximum);
        */
        setUser(user);
        
        setButton(BUTTON_NEGATIVE, resources.getString(R.string.return_), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 cancel();
            }
        });

        chooseButton.setOnClickListener(chooseListener);
        editButton.setOnClickListener(editListener);
        deleteButton.setOnClickListener(deleteListener);
        
        setView(v);
    }
    
    public void setUser(User user)
    {
        setTitle(resources.getString(R.string.user) + ": " + user.getName());
        setIcon(user.getPhoto());
        boolean isCustom = user.isCustom();
        editButton.setEnabled(isCustom);
        deleteButton.setEnabled(isCustom);
    }
    
    private android.view.View.OnClickListener chooseListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.chooseCurrentUser();
                dismiss();
            }
        };
    
    private android.view.View.OnClickListener editListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.showEditUserDialog();
                dismiss();
            }
        };
    
    private android.view.View.OnClickListener deleteListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.showDeleteUserDialog();
                dismiss();
            }
        };

    
}
