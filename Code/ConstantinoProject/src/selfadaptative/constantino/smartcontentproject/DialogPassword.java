/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Dialog password for encryption/decryption
 */

package selfadaptative.constantino.smartcontentproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogPassword extends DialogFragment {

	private ActivityFile act;
	private String password;
	private EditText textfield;
	private boolean done;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	password = "";
    	done = false;
    	
    	act = (ActivityFile) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater factory = LayoutInflater.from(act);
        //use view defined to have an editText
        View view = factory.inflate(R.layout.dialog_password, null);
        builder.setTitle(R.string.dialog_password);
        builder.setView(view);
        
        textfield = (EditText) view.findViewById(R.id.field_password);

        //BUTTONS
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {   
                	   password = textfield.getText().toString(); //get string
                	   done = true;
                   }
               });
        
        builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    public void onDismiss(DialogInterface dialog){
    	super.onDismiss(dialog);
    	if (done)
    		act.eventPassword(true, password);     
    	else
    		act.eventPassword(false, null);
    }
}