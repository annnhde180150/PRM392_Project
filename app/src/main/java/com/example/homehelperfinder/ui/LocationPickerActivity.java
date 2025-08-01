package com.example.homehelperfinder.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.BuildConfig;
import com.example.homehelperfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.common.api.Status;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker selectedMarker;
    private LatLng selectedLatLng;
    private String line1, city, district, ward, fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY, Locale.getDefault()); // or BuildConfig.MAPS_API_KEY
        }

        // Setup AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(
                    Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS
            ));
            // Restrict to Vietnam
            autocompleteFragment.setCountries("VN");
            // Bias toward Da Nang
            autocompleteFragment.setLocationBias(
                RectangularBounds.newInstance(
                    new LatLng(15.95, 108.10), 
                    new LatLng(16.15, 108.30)  
                )
            );
            // Only show addresses
            autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    if (place.getLatLng() != null && mMap != null) {
                        selectedLatLng = place.getLatLng();
                        if (selectedMarker != null) selectedMarker.remove();
                        selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedLatLng).title(place.getName()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 17f));
                        fetchAddressFromLatLng(selectedLatLng);
                    }
                }
                @Override
                public void onError(@NonNull Status status) {
                    // Optionally handle error
                }
            });
        }

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
                resultIntent.putExtra("line1", line1);
                resultIntent.putExtra("fullAddress", fullAddress);
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
                city = address.getAdminArea();
                district = address.getSubAdminArea();
                ward = address.getSubLocality();
                String streetNumber = address.getSubThoroughfare();
                String streetName = address.getThoroughfare();
                line1 = (streetNumber != null ? streetNumber + " " : "") + (streetName != null ? streetName : "");
                if (line1.isEmpty()) {
                    line1 = address.getAddressLine(0);
                }
                fullAddress = address.getAddressLine(0);
            } else {
                line1 = city = district = ward = null;
            }
        } catch (Exception e) {
            line1 = city = district = ward = null;
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