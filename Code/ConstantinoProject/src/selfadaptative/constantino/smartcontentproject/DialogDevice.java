/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Dialog for choosing type of device info
 */

package selfadaptative.constantino.smartcontentproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class DialogDevice extends DialogFragment {

	private ActivityFile act;
	private CheckBox checkDeviceManufacturer;
	private CheckBox checkDeviceModel;
	private boolean done;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	done = false;
    	
    	act = (ActivityFile) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater factory = LayoutInflater.from(act);

        View view = factory.inflate(R.layout.dialog_device, null);
        builder.setTitle(R.string.dialog_device_title);
        builder.setView(view);

        checkDeviceManufacturer = (CheckBox) view.findViewById(R.id.checkDeviceManufacturer);
        checkDeviceModel = (CheckBox) view.findViewById(R.id.checkDeviceModel);

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
    		act.eventDevice(true, checkDeviceManufacturer.isChecked(), checkDeviceModel.isChecked());     
    	else
    		act.eventDevice(false, false, false);
    }
    
}