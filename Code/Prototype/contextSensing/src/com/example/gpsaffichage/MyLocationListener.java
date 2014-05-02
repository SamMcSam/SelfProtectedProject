package com.example.gpsaffichage;

import android.app.Activity;
import android.content.Context;
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;
import android.os.Bundle;  
  
public class MyLocationListener implements LocationListener {
    
    private LocationManager locationManager;
    public String longitude;
    public String latitude;
    private Location mCurrentLocation;
    
    public MyLocationListener(Activity activity) {
    	this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    	mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
     
    public void updateLocation(){
    	mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	latitude = "Latitude: "+mCurrentLocation.getLatitude();
    	longitude = "Longitude: "+mCurrentLocation.getLongitude();
    }
    
    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
    	updateLocation();    
    	//longitude = "Longitude: "+location.getLongitude();
    	//latitude = " Latitude: "+location.getLatitude();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
         
        /******** Called when User off Gps *********/
    	longitude = "Error";
    	latitude = " Gps is disabled" ;
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    	updateLocation();
    }

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		updateLocation();
	}
}