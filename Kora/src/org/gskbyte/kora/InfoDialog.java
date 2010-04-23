package org.gskbyte.kora;

import org.gskbyte.kora.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class InfoDialog extends AlertDialog {
	
	private static final String TAG = "InfoDialog";
	
	//Constructor
	public InfoDialog(Context context) {
		super(context);
		
		View v = View.inflate(context, R.layout.info_dialog, null);
		
		setView(v);
		
		Resources r = context.getResources();
		String title = r.getString(R.string.appName)+" "+r.getString(R.string.versionNumber);
		this.setTitle(title);
		this.setIcon(r.getDrawable(R.drawable.icon_info));
		/*
		setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dismiss();
            }
         });
         */
	}
}
