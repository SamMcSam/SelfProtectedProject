/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 *
 * Decoding activity
 * Waiting thread are launched for each context
 */
package selfadaptative.constantino.smartcontentproject;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;


public class ActivityFileDecode extends ActivityFile {

	private final static int DELAY_WAITING = 1000;
	private final static int WAITING_MAX_NETWORK = 5;
	private final static int WAITING_MAX_GPS = 20;
	
	private SmartFile file = null;
	
	private ContextGPS contextGPS = null;
	private ContextDevice contextDevice = null;
	private ContextNetwork contextNetwork = null;
	//private ContextDate contextDate = null;
	
	private FragmentManager mFragments = null;
	private DialogPassword dialogPassword = null;
	private ProgressDialog loadingDialog = null;

	private Handler handler = null;
	
	private int waitingNetworkWifi = WAITING_MAX_NETWORK;
	private int waitingGPS = WAITING_MAX_GPS;
	
	//for sequential trial of context
	private int index = 0;
	private boolean done = false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_decode);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		file = (SmartFile) this.getIntent().getSerializableExtra(ActivityContent.BUNDLE_FILE);
		
		contextGPS = new ContextGPS(this);
    	contextNetwork = new ContextNetwork(this);
    	contextDevice = new ContextDevice();
    	//contextDate = new ContextDate();

    	//popup menus
    	mFragments = getSupportFragmentManager();
    	dialogPassword = new DialogPassword();
    	dialogPassword.setCancelable(false);
    	loadingDialog = new ProgressDialog(this);
    	loadingDialog.setMessage(getResources().getString(R.string.decoding_wait));
    	loadingDialog.setCancelable(false);
    	
		handler = new Handler();		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
    	if (id == android.R.id.home){
    		this.finish();
            return true;
    	}
		return super.onOptionsItemSelected(item);
	}
	
	//---------------------------------------------------------
  	// Activity flow
  	//---------------------------------------------------------
	
	@Override
    protected void onPause() {
        super.onPause();
        //stop the delaying thread
        handler.removeCallbacksAndMessages(null);
        if (dialogPassword.isVisible())
        	dialogPassword.dismiss();
        
        contextGPS.stop();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //start the thread
        //startThread();
        
        contextGPS.start();
        
        //Get the contexts
        if (index < file.getPolicies().size())
        	senseContext();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //this.saveState(); //to make sure saved modifications
        outState.putSerializable(ActivityContent.BUNDLE_FILE, (Serializable) file);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      this.file = (SmartFile) savedInstanceState.getSerializable(ActivityContent.BUNDLE_FILE);
    }
    
    @Override
    public void finish(){
        contextGPS.stop();
    	handler.removeCallbacksAndMessages(null);
        super.finish();
        if (!done)
        	Toast.makeText(this, getResources().getString(R.string.decoding_impossible), Toast.LENGTH_LONG).show();
    }
	
	//---------------------------------------------------------
  	// Decoding
  	//---------------------------------------------------------
	
	public void uncrypt(){
		//stop loading screen
		//dialogProgress.dismiss();
		
		//if worked	
		if (file.decodeFile(this)){
			Intent i = new Intent(this, ActivityContent.class);
			i.putExtra(ActivityContent.BUNDLE_FILE, file);
			
			done = true;
			this.finish();
			startActivity(i);
		}
		//or just finish with toast explaining not possible
		else{
			this.finish();
		}
	}
	
	//---------------------------------------------------------
  	// User input
  	//---------------------------------------------------------
	
	public void eventPassword(boolean done, String password) {
		if (done){
			for (int i=0;i<file.getPolicies().size();i++){
				if (file.getPolicies().get(i).getType() == EnumPolicyType.password){
					file.getPolicies().get(i).setContext(password);
					nextIndex();
				}
			}
		}
		else{
			this.finish(); //if Cancel, quit
			//sensePassword(); //or ask again?
		}
	}
	
	//---------------------------------------------------------
  	// Functions for context sensing
  	//---------------------------------------------------------
	
	public void nextIndex(){
		index++;
		if (index >= file.getPolicies().size())
			uncrypt();
		else
			senseContext();
	}
	
	//for all policies in file, do appropriate thing (asking for context)
	public void senseContext(){	
		//if policies (not draft)
		if (!file.getPolicies().isEmpty()){
			switch (file.getPolicies().get(index).getType())
			{
			case password :
				sensePassword();
				break;
			case gps_coordinates :
				senseGPS();
				break;
			case network_current_wifi :
				senseNetworkCurrentWifi();
				break;
			case device_manufacturer :
				senseDeviceManufacturer();
				break;
			case device_model :
				senseDeviceModel();
				break;
			default:
				break;
			}

		}	
		else{
			Toast.makeText(this, "Not a smartdoc?", Toast.LENGTH_LONG).show();
			this.finish();
		}
	}

	//in case of password, has to open a dialog
	public void sensePassword(){
		if (file.getPolicies().get(index).getContext().equals("")){
			dialogPassword.show(mFragments, null);
			//nextIndex(); done after event dialog
		} 		
	}

	//in case of network, waits max x seconds, each sec request network
	public void senseNetworkCurrentWifi(){		
		//loading popup
		if (!loadingDialog.isShowing())
			loadingDialog.show();
		
		//Tries to get the network during a certain period of time
		if(waitingNetworkWifi > 0){
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		        	 String currentWifi = contextNetwork.getCurrentNetwork();
		        	 waitingNetworkWifi--;
		        	 
		        	 //if network is connected
		        	 if (contextNetwork.isOkay()){
		        		file.getPolicies().get(index).setContext(currentWifi);
	        		 	if (!file.getPolicies().get(index).getContext().equals(""))
		     		    	waitingNetworkWifi = 0;		
		        	 }
		        	 
		        	 senseNetworkCurrentWifi();
		         } 
		    }, DELAY_WAITING);     	
		}
		else{
			nextIndex();
			loadingDialog.dismiss();
		}
	}
	
	//in case of gps, waits max x seconds, each sec request network
		public void senseGPS(){		
			//loading popup
			if (!loadingDialog.isShowing())
				loadingDialog.show();
			
			//Tries to get the coordinates during a certain period of time
			if(waitingGPS > 0){
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 waitingGPS--;
			        	 
			        	 //if network is connected
			        	 if (contextGPS.isOkay()){
			        		 int scale = Integer.parseInt(file.getPolicies().get(index).getProperty());
			        		 file.getPolicies().get(index).setContext(contextGPS.getLatitude(scale)+getResources().getString(R.string.policy_gps_separator)+contextGPS.getLongitude(scale));
			        		 if (!file.getPolicies().get(index).getContext().equals(""))
			        		 		waitingGPS = 0;	
		    			}
			        	 
			        	 senseGPS();
			         } 
			    }, DELAY_WAITING);     	
			}
			else{
				nextIndex();
				loadingDialog.dismiss();
			}
		}
	
	public void senseDeviceManufacturer(){
		file.getPolicies().get(index).setContext(contextDevice.getManufacturer());
		nextIndex();
	}
	
	public void senseDeviceModel(){
		file.getPolicies().get(index).setContext(contextDevice.getBuild());	
		nextIndex();
	}
	
}
