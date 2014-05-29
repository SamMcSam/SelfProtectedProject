/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Dialog for choosing the range of the GPS
 */

package selfadaptative.constantino.smartcontentproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DialogGPS extends DialogFragment implements OnSeekBarChangeListener {
	
	public static final int ROUGH_ESTIMATE = 5;
	
	private ActivityFile act;

	private SeekBar gpsScale;
	private TextView textRange;

	private int value;
	private boolean done;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	done = false;
    	
    	act = (ActivityFile) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater factory = LayoutInflater.from(act);

        View view = factory.inflate(R.layout.dialog_gps, null);
        builder.setTitle(R.string.dialog_gps_title);
        builder.setView(view);

        gpsScale = (SeekBar) view.findViewById(R.id.dialog_gps_scale);
        textRange = (TextView) view.findViewById(R.id.dialog_text_range);
        gpsScale.setOnSeekBarChangeListener(this);
        
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
    		act.eventGPS(true, value);     
    	else
    		act.eventGPS(false, 0);
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		value = progress;
		
		//THIS IS NOT A REAL WORLD CONVERSION!!!!! this is a rough estimate from empirical data (it will only work in Switzerland!)
		//See more here : http://en.wikipedia.org/wiki/Geographic_coordinate_system#Expressing_latitude_and_longitude_as_linear_units		
		int estimate = (getResources().getInteger(R.integer.max_scale) - progress + 1) * ROUGH_ESTIMATE;
		textRange.setText(getResources().getString(R.string.dialog_gps_range)+" "+estimate+getResources().getString(R.string.dialog_gps_mesure)); 
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {	
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {		
	}
    
}