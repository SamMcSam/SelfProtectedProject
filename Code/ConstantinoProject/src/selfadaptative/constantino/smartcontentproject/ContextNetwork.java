package selfadaptative.constantino.smartcontentproject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

public class ContextNetwork {
	
	private String currentNetwork;
	private Activity ctx;
	private boolean okay;

	public ContextNetwork(Activity ctx){
		this.currentNetwork = "";
		this.ctx = ctx;
		this.okay = false;
	}
	
	public boolean isOkay(){
		return okay;
	}
	
	//method from http://stackoverflow.com/questions/8811315/how-to-get-current-wifi-connection-info-in-android
	public String getCurrentNetwork(){
		currentNetwork = "";
		
		ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (networkInfo.isConnected()) {
			WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
				currentNetwork = connectionInfo.getSSID();
				okay = true;
			}
		}
		else
			okay = false;
		
	  return currentNetwork;
	}
	
	/////////////
	// DEPRECATED
	public ArrayList<String> getListNetwork(){
		WifiManager wifi;   
		ArrayList<String> list = new ArrayList<String>();
		
		//initialize wifi
		wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(ctx, "Wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        } 
        Toast.makeText(ctx, "Scanning....", Toast.LENGTH_SHORT).show();
		wifi.startScan();
        
		List<ScanResult> results = wifi.getScanResults();

        for (int i=0;i<results.size();i++)
        {    
        	list.add(results.get(i).SSID);             
        } 
		
		return list;
	}

}
