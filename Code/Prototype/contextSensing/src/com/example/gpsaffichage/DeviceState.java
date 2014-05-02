package com.example.gpsaffichage;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceState extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);

		Calendar c = Calendar.getInstance(); 
		
		TextView viewDate = (TextView) findViewById(R.id.textView1);
		TextView viewManufacturer = (TextView) findViewById(R.id.textView2);
		TextView viewBuild = (TextView) findViewById(R.id.textView3);
		
		viewDate.setText(c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.YEAR));
		viewManufacturer.setText(android.os.Build.MANUFACTURER);
		viewBuild.setText(android.os.Build.MODEL);
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
				Toast.makeText(this, "GPS selected", Toast.LENGTH_SHORT)
		          .show();
				finish();
				i = new Intent(this, GPSActivity.class);
				startActivity(i);
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
	
}
