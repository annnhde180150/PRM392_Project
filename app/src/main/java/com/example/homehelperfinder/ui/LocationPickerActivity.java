package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker selectedMarker;
    private LatLng selectedLatLng;
    private String city, district, ward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSelectLocation.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                // Always fetch address before returning
                fetchAddressFromLatLng(selectedLatLng);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLatLng.latitude);
                resultIntent.putExtra("longitude", selectedLatLng.longitude);
                resultIntent.putExtra("city", city);
                resultIntent.putExtra("district", district);
                resultIntent.putExtra("ward", ward);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void fetchAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                city = address.getAdminArea(); // City
                district = address.getSubAdminArea(); // District
                ward = address.getSubLocality(); // Ward (may be null)
            } else {
                city = district = ward = null;
            }
        } catch (Exception e) {
            city = district = ward = null;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Set default camera to Da Nang, Vietnam
        LatLng daNang = new LatLng(16.0471, 108.2068);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(daNang, 13f));
        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            fetchAddressFromLatLng(latLng);
        });
    }
} 