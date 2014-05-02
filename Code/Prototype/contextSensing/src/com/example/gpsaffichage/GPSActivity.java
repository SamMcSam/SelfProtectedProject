package com.example.gpsaffichage;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GPSActivity extends ActionBarActivity {

	private Calendar c = null;
	private MyLocationListener location;
	
	private Thread myThread = null;
	
	private TextView viewLongitude = null;
	private TextView viewLatitude = null;
	private TextView viewTime = null;
	
	public void updateAffichage(){
		c = Calendar.getInstance(); 		
		viewTime.setText("Time : " + c.get(Calendar.HOUR) + "h " + c.get(Calendar.MINUTE) + "h " + c.get(Calendar.SECOND) + "s");
		
		location.updateLocation();
		viewLongitude.setText(location.longitude);
		viewLatitude.setText(location.latitude);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		location = new MyLocationListener(this);

		viewTime = (TextView) findViewById(R.id.textView1);
		viewLatitude = (TextView) findViewById(R.id.textView2);
		viewLongitude = (TextView) findViewById(R.id.textView3);
	}

	//---------------------------------------------------
	// Pour update
	
	public void startTheBizness(){
		Runnable myRunnableThread = new CountDownRunner();
	    myThread= new Thread(myRunnableThread);   
	    myThread.start();
	}
	
	public void doWork() {
	    runOnUiThread(new Runnable() {
	        public void run() {
	            try{
	            	updateAffichage();
	            }catch (Exception e) {}
	        }
	    });
	}


	class CountDownRunner implements Runnable{
	    // @Override
	    public void run() {
	            while(!Thread.currentThread().isInterrupted()){
	                try {
	                doWork();
	                    Thread.sleep(1000); // Pause of 1 Second
	                } catch (InterruptedException e) {
	                        Thread.currentThread().interrupt();
	                }catch(Exception e){
	                }
	            }
	    }
	}
	
	//-----------------------------------------------------
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = null;
		
		switch (item.getItemId()) {		
			case R.id.action_gps:
				break;
		    case R.id.action_network:
			      Toast.makeText(this, "Network selected", Toast.LENGTH_SHORT)
			          .show();
			      finish();
			      i = new Intent(this, ListOfNetwork.class);
			      i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			      startActivity(i);
			      break;      
		    case R.id.action_device:
			      Toast.makeText(this, "Device selected", Toast.LENGTH_SHORT)
			          .show();
			      finish();
			      i = new Intent(this, DeviceState.class);
			      i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			      startActivity(i);
			      break;
		    case R.id.action_settings:
		      Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
		          .show();
		      break;
		    default:
		      break;
	    }
		return super.onOptionsItemSelected(item);
	}

	//---------------------------------------------------
	
	@Override
    protected void onPause() {
        super.onPause();
        try {
    		Thread.currentThread().interrupt();
    		myThread = null;
    	}
    	catch(Exception e){
    		//prout
    	}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        startTheBizness();
    }

}
