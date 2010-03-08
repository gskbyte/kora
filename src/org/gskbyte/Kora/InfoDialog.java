package org.gskbyte.Kora;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoDialog extends AlertDialog {
	
	private static final String TAG = "InfoDialog";
		
	private ImageView mImageView;
	private TextView mProjectTextView, mVersionTextView, mAuthorTextView, mCollaboratorsTextView;
	
	//Constructor
	public InfoDialog(Context context) {
		super(context);
		
		View v = View.inflate(context, R.layout.info_dialog, null);
		
		setView(v);
		
		setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dismiss();
            }
         });
	}
}
