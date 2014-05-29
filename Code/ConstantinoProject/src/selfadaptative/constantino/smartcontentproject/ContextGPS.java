/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Location listener (Treat data in grid like manner - so that data in same cell (but not same) are equal)
 */

package selfadaptative.constantino.smartcontentproject;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
  
public class ContextGPS implements LocationListener {

	public static final int MULTIPLIER = 10000000;
	public static final int MAX_LAT = 90;
	public static final int MAX_LONG = 180;
	public static final int PROP_CONVERT = 5000;
	
    public LocationManager locationManager;
    private Double longitude;
    private Double latitude;
    private boolean okay;
    
	public ContextGPS(Activity activity) {
    	this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

    	longitude = null;
    	latitude = null;
    	okay = false;
    }
	
	//---------------------------------------------------------
  	// get data
  	//---------------------------------------------------------
	
	public String getLatitude(int property){
		if (property > 0)
			//fit data in grid like manner
			return ""+ (int)(((float) (property * PROP_CONVERT) / (MAX_LAT*MULTIPLIER) * (this.latitude*MULTIPLIER)));
		else{
			if (this.latitude == null)
				return "";
			else
				return ""+this.latitude;
		}	
	}
	
	public String getLongitude(int property){
		if (property > 0)
			//fit data in grid like manner
			return ""+(int)(((float)(property * PROP_CONVERT) / (MAX_LONG*MULTIPLIER) * (this.longitude*MULTIPLIER)));
		else{
			if (this.longitude == null)
				return "";
			else
				return ""+this.longitude;
		}
	}
	
	public boolean isOkay(){
		return okay;
	}
	
	//---------------------------------------------------------
  	// Functions for requesting location
  	//---------------------------------------------------------
    
    public void stop(){
    	this.locationManager.removeUpdates(this);
    }
    
    public void start(){
    	this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    	this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
    }
    
    //Called after each sec
    @Override
    public void onLocationChanged(Location location) {
    	okay = true;
    	latitude = location.getLatitude(); 
    	longitude = location.getLongitude();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // Called when User off Gps
    	okay = false;
    	latitude = null;
    	longitude = null;
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	okay = false;
    	latitude = null;
    	longitude = null;
    }

	@Override
	public void onProviderEnabled(String provider) {
		okay = false;
		latitude = null;
    	longitude = null;
	}
	
}