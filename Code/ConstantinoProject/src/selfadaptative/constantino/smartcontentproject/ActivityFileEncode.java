/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 *
 * Encoding activity
 * Thread updates the context info
 * Check boxes are only available if context is also
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityFileEncode extends ActivityFile {

	private SmartFile file = null;
	
	private TextView viewDate = null;
	private TextView viewTime = null;
	private TextView viewLatitude = null;
	private TextView viewLongitude = null;
	private TextView viewWifi = null;
	private TextView viewManufacturer = null;
	private TextView viewModel = null;

	private RadioButton radioRead = null;
	private RadioButton radioWrite = null;
	private CheckBox checkPassword = null;
	private CheckBox checkGPS = null;
	private CheckBox checkNetwork = null;
	private CheckBox checkDevice = null;
	private Button buttonEncode = null;
	
	private ContextGPS contextGPS = null;
	private ContextDevice contextDevice = null;
	private ContextNetwork contextNetwork = null;
	private ContextDate contextDate = null;
	
	private Thread threadUpdate = null; //another thread updates the displayed date continuously
	
	private FragmentManager mFragments = null;
	private DialogPassword dialogPassword = null;
	private DialogGPS dialogGPS = null;
	private DialogDevice dialogDevice = null;
	private DialogNetwork dialogNetwork = null;
	
	//---------------------------------------------------------
  	// Setup
  	//---------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_encode);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		viewDate = (TextView) findViewById(R.id.textDate);
		viewTime = (TextView) findViewById(R.id.textTime);
		viewLatitude = (TextView) findViewById(R.id.textGPSLat);
		viewLongitude = (TextView) findViewById(R.id.textGPSLong);
		viewWifi = (TextView) findViewById(R.id.textNetworkWIFI);
		viewManufacturer = (TextView) findViewById(R.id.textDevManuf);
		viewModel = (TextView) findViewById(R.id.textDevMod);
		radioRead = (RadioButton) findViewById(R.id.radio_read);
		radioWrite = (RadioButton) findViewById(R.id.radio_write);
		checkPassword = (CheckBox) findViewById(R.id.checkboxPassword);
		checkGPS = (CheckBox) findViewById(R.id.checkboxGPS);
		checkNetwork = (CheckBox) findViewById(R.id.checkboxNetworkWIFI);
		checkDevice = (CheckBox) findViewById(R.id.checkboxDevice);
		buttonEncode = (Button) findViewById(R.id.confirm);
		
		this.file = (SmartFile) this.getIntent().getSerializableExtra(ActivityContent.BUNDLE_FILE);
		//in case not done - or error?
		if (this.file.getTitle().equals(""))
			this.file.setTitle(getResources().getString(R.string.empty_title));
		if (this.file.getContentClear().equals(""))
			this.file.setContentClear(getResources().getString(R.string.empty_content));
			
    	contextGPS = new ContextGPS(this);
    	contextNetwork = new ContextNetwork(this);
    	contextDevice = new ContextDevice();
    	contextDate = new ContextDate();
    	
    	//popup menus
    	mFragments = getSupportFragmentManager();
    	dialogPassword = new DialogPassword();
    	dialogDevice = new DialogDevice();
    	dialogNetwork = new DialogNetwork();
    	dialogGPS = new DialogGPS();
    	
	}	
	
	//---------------------------------------------------------
  	// Encoding action
  	//---------------------------------------------------------
    
    //button only works if policy saved - so can only save as smartdoc (not as draft....)
    public void encodeAndSave(View view) {
    	this.file.saveTheFile(this); 
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        
        Toast.makeText(this, getResources().getString(R.string.encoding_done) + " " + this.file.getTitle() + getResources().getString(R.string.extension_file), Toast.LENGTH_LONG).show();
        
    	this.finish();
    }
	
    //---------------------------------------------------------
  	// Event for checkbox and radio button
  	//---------------------------------------------------------
	
    private void populateFields() {
        //put check box has specified in this.file
		for (int i=0;i<file.getPolicies().size();i++){
			switch (file.getPolicies().get(i).getType())
			{
			case password :
				checkPassword.setChecked(true);
				break;
			case gps_coordinates :
				checkGPS.setChecked(true);
				break;
			case network_current_wifi :
				checkNetwork.setChecked(true);
				break;
			case device_manufacturer : case device_model :
				checkDevice.setChecked(true);
				break;
			default:
				break;
			}
		}
		
		if (file.isAction_write()){
			radioRead.setChecked(false);
			radioWrite.setChecked(true);
		}
		else{
			radioRead.setChecked(true);
			radioWrite.setChecked(false);
		}
		
		verifyStateButton();
	}
	
	public void updateAffichage(){
		//TEXT in context
		viewTime.setText(getResources().getString(R.string.encode_display_time) + " " + contextDate.getTime());
		viewDate.setText(getResources().getString(R.string.encode_display_date) + " " + contextDate.getDate());
		viewLatitude.setText(getResources().getString(R.string.encode_display_gps_latitude) + " " + contextGPS.getLatitude(0));
		viewLongitude.setText(getResources().getString(R.string.encode_display_gps_longitude) + " " + contextGPS.getLongitude(0));
		viewWifi.setText(getResources().getString(R.string.encode_display_network_wifi) + " " + contextNetwork.getCurrentNetwork());
		viewManufacturer.setText(getResources().getString(R.string.encode_display_device_manufacturer) + " " + contextDevice.getManufacturer());
		viewModel.setText(getResources().getString(R.string.encode_display_device_model) + " " + contextDevice.getBuild());
		
		//STATE of buttons
		if (contextGPS.isOkay())
			checkGPS.setEnabled(true);
		else
			checkGPS.setEnabled(false);
		if (contextNetwork.isOkay())
			checkNetwork.setEnabled(true);
		else
			checkNetwork.setEnabled(false);
		//if new check box - add here (or redo this code in a more dynamic way...?)
	}
    
    public void verifyStateButton(){
    	//activate encode button if at least one checked
		if (checkPassword.isChecked() || checkGPS.isChecked() || checkNetwork.isChecked() || checkDevice.isChecked())
			buttonEncode.setEnabled(true);
		else
			buttonEncode.setEnabled(false);
    }
    
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_read:
                if (checked)
                	this.file.setAction_write(false);
                else
                	this.file.setAction_write(true);
                break;
            case R.id.radio_write:
                if (checked)
                	this.file.setAction_write(true);
                else
                	this.file.setAction_write(false);
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        
        switch(view.getId()) {
            case R.id.checkboxPassword:
            	if (checked)
            		dialogPassword.show(mFragments, null);
            	else
            		this.file.removePolicy(EnumPolicyType.password);
            		verifyStateButton();
                break;
            case R.id.checkboxGPS:
            	if (checked)
            		dialogGPS.show(mFragments, null);
            	else
            		this.file.removePolicy(EnumPolicyType.gps_coordinates);
            		verifyStateButton();
                break;
            case R.id.checkboxNetworkWIFI:
            	if (checked)
            		dialogNetwork.show(mFragments, null);
            	else
            		this.file.removePolicy(EnumPolicyType.network_current_wifi);
            		verifyStateButton();
                break;
            case R.id.checkboxDevice:
            	if (checked)
            		dialogDevice.show(mFragments, null);
            	else
            		this.file.removePolicy(EnumPolicyType.device_manufacturer);
        			this.file.removePolicy(EnumPolicyType.device_model);
            		verifyStateButton();
                break;
        }
    }
    
    //For result of Password dialog
    public void eventPassword(boolean done, String password){
    	if (done){
    		if (password.equals(""))
    			checkPassword.setChecked(false); //in case no password given
    		else{
        		Policy policy = new Policy(EnumPolicyType.password);
        		policy.setContext(password);
        		this.file.addPolicy(policy);
    		}
    	}
    	else{
    		checkPassword.setChecked(false);
    	}
    	verifyStateButton();
    }
    
    public void eventDevice(boolean done, boolean manufacturer, boolean model){
    	if (done){
    		if (!manufacturer & !model) {
    			checkDevice.setChecked(false); //in case no box checked
    		}
    		else{
    			if (manufacturer){
    				Policy policy = new Policy(EnumPolicyType.device_manufacturer);
    	    		policy.setContext(contextDevice.getManufacturer());
    	    		this.file.addPolicy(policy);
    			}
    			if (model){
    				Policy policy = new Policy(EnumPolicyType.device_model);
    	    		policy.setContext(contextDevice.getBuild());
    	    		this.file.addPolicy(policy);
    			}
    		}
    	}
    	else{
    		checkDevice.setChecked(false);
    	}
    	verifyStateButton();
    }
    
    public void eventNetwork(boolean done,  boolean currentWifi){
    	if (done){
    		if (!currentWifi) {
    			checkNetwork.setChecked(false); //in case no box checked
    		}
    		else{
    			String thisWifi = contextNetwork.getCurrentNetwork();
    			if (contextNetwork.isOkay()){
    				Policy policy = new Policy(EnumPolicyType.network_current_wifi);
    	    		policy.setContext(thisWifi);
    	    		this.file.addPolicy(policy);
    			}
    			else{
    				checkNetwork.setChecked(false); //in case network as been disconnected since
    			}
    		}
    	}
    	else{
    		checkNetwork.setChecked(false);
    	}
    	verifyStateButton();
    }
    
    public void eventGPS(boolean done, int scale){
    	if (done){
    		if (scale > 0) {
    			if (contextGPS.isOkay()){
    				Policy policy = new Policy(EnumPolicyType.gps_coordinates, Integer.toString(scale));
    				//format is lat#long
    	    		policy.setContext(contextGPS.getLatitude(scale)+getResources().getString(R.string.policy_gps_separator)+contextGPS.getLongitude(scale)); 
    	    		this.file.addPolicy(policy);
    			}
    			else{
    				checkGPS.setChecked(false); //in case network as been disconnected since
    			}
    		}
    		else{
    			checkGPS.setChecked(false); //in case too small	
    		}
    	}
    	else{
    		checkGPS.setChecked(false);
    	}
    	verifyStateButton();
    }    
	//---------------------------------------------------------
  	// Activity flow
  	//---------------------------------------------------------
	
	@Override
    protected void onPause() {
        super.onPause();
        //stop the displaying thread
        this.stopThread();
        
        //this.saveState();
        contextGPS.stop();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //put info of the file
        populateFields();
        
        //start the thread
        startThread();
        
        contextGPS.start();
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
        this.stopThread();
        super.finish();
    }
    
    //---------------------------------------------------------
  	// Displaying thread
  	//---------------------------------------------------------
    
    public void startThread(){
		Runnable runnableThread = new UpdateThread();
		threadUpdate= new Thread(runnableThread);   
		threadUpdate.start();
	}
	
    public void stopThread(){
    	try {
    		Thread.currentThread().interrupt();
    		threadUpdate = null;
    	}catch(Exception e){}
    }

	class UpdateThread implements Runnable{
	    public void run() {
	        while(!Thread.currentThread().isInterrupted()){
	            try {
	            	actionThread();
	                Thread.sleep(1000); // Pause of 1 Second
	            } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	            }catch(Exception e){
	            }
	        }
	    }
	}
    
	public void actionThread() {
	    runOnUiThread(new Runnable() {
	        public void run() {
	            try{
	            	updateAffichage();
	            }catch (Exception e) {}
	        }
	    });
	}
	
	//---------------------------------------------------------
  	// Option Menu
  	//---------------------------------------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.encode, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Toast.makeText(this, getResources().getString(R.string.encoding_cancelled), Toast.LENGTH_LONG).show();
	        Intent returnIntent = new Intent();
	        setResult(RESULT_CANCELED, returnIntent);
			finish();
			return true;
		}
		if (id == R.id.action_help) {
			Intent i = new Intent(this, ActivityHelp.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
}
