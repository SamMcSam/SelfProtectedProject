/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Dialog for choosing type of network
 * (only one type anymore)
 */

package selfadaptative.constantino.smartcontentproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

public class DialogNetwork extends DialogFragment {

	private ActivityFile act;

	private RadioButton radioCurrentWifi; //to be changed to checkbox if implement more network options...
	private boolean done;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	done = false;
    	
    	act = (ActivityFile) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater factory = LayoutInflater.from(act);

        View view = factory.inflate(R.layout.dialog_network, null);
        builder.setTitle(R.string.dialog_network_title);
        builder.setView(view);

        radioCurrentWifi = (RadioButton) view.findViewById(R.id.radio_wifi_current);
        
        //BUTTONS
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {   
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
    		act.eventNetwork(true, radioCurrentWifi.isChecked());     
    	else
    		act.eventNetwork(false, false);
    }
    
}