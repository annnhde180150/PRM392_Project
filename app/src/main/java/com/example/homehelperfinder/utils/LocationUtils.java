package com.example.homehelperfinder.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

public class LocationUtils {
    
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }
    
    public static void getCurrentLocation(Context context, LocationCallback callback) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
        if (locationManager == null) {
            callback.onLocationError("Location service not available");
            return;
        }
        
        // Check permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationError("Location permission not granted");
            return;
        }
        
        // Check if GPS or Network provider is enabled
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if (!isGPSEnabled && !isNetworkEnabled) {
            callback.onLocationError("GPS and Network location are disabled");
            return;
        }
        
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                } else {
                    callback.onLocationError("Unable to get location");
                }
            }
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            
            @Override
            public void onProviderEnabled(String provider) {}
            
            @Override
            public void onProviderDisabled(String provider) {}
        };
        
        try {
            // Try to get last known location first
            Location lastKnownLocation = null;
            
            if (isGPSEnabled) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            
            if (lastKnownLocation == null && isNetworkEnabled) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            
            if (lastKnownLocation != null) {
                callback.onLocationReceived(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            } else {
                // Request location updates
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener, Looper.getMainLooper());
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener, Looper.getMainLooper());
                }
            }
            
        } catch (Exception e) {
            callback.onLocationError("Error getting location: " + e.getMessage());
        }
    }
    
    /**
     * Calculate distance between two points in kilometers
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000; // Convert meters to kilometers
    }
} 