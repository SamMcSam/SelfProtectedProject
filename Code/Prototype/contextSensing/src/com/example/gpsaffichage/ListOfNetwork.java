package com.example.gpsaffichage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfNetwork extends Activity implements OnClickListener {
	   
    private WifiManager wifi;       
    private ListView lv;
    //private TextView textStatus;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;

    ArrayList<String> item = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.list);
        
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        } 

        //refresh();
 
    }
    
    public void refresh(){
    	item.clear();          
        wifi.startScan();
        
        results = wifi.getScanResults();
        size = results.size();
        
        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
        
        try 
        {	
            for (int i=0;i<results.size();i++)
            {    
                item.add(results.get(i).SSID);             
            } 
        }
        catch (Exception e)
        { }    
        
		String[] from =  item.toArray(new String[item.size()]);
        
        this.adapter = new ArrayAdapter<String>(this,R.layout.list_row, R.id.text1, from);
        lv.setAdapter(this.adapter);  
    }
    
    @Override
	public void onClick(View arg0) {
    	refresh();
	}
    
    //----------------------------------------------------
    
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
				Toast.makeText(this, "Network selected", Toast.LENGTH_SHORT)
		          .show();
				finish();
				i = new Intent(this, GPSActivity.class);
				startActivity(i);
				break;
		    case R.id.action_network:
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


}