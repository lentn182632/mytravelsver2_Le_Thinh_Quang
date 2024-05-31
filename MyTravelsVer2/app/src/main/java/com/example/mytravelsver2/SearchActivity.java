package com.example.mytravelsver2;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient; /*variable for get your location*/
    private SearchView mapSearchView; /*search on map*/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); /*get location from api of Google Play*/
        getLastLocation();

        mapSearchView = findViewById(R.id.mapSearch);/*search on map*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);/*search on map*/

//        setupPlacesAutocomplete(); // Call the function to set up Places API autocomplete
        /*search on map*/
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchOnMap(query); //Return false to indicate that you have handled the submit action
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        mapFragment.getMapAsync(MainActivity.this);
    }
    private void searchOnMap(String location) {

        List<Address> addressList = null;
        if (location != null){
            Geocoder geocoder = new Geocoder(SearchActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location,1);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            if (addressList != null && !addressList.isEmpty()){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }else {
                // Handle the case when no address is found
                Toast.makeText(SearchActivity.this,"Location not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(SearchActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        MarkerOptions options = new MarkerOptions().position(sydney).title("My location");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        myMap.addMarker(options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
//    private void setupPlacesAutocomplete() {
//        // Initialize Places API
//        Places.initialize(getApplicationContext(), "AIzaSyCpTrodY4YyjDKTj22XcxBoKFMwh4B-MjM");
//        PlacesClient placesClient = Places.createClient(this);
//
//        // Create AutocompleteSupportFragment
//        AutocompleteSupportFragment autocompleteFragment = new AutocompleteSupportFragment.Builder()
//                .setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
//                .build();
//
//        // Add AutocompleteSupportFragment to the FrameLayout
//        getSupportFragmentManager().beginTransaction().replace(R.id.autocompleteContainer, autocompleteFragment).commit();
//
//        autocompleteFragment.setOnPlaceSelectedListener(new AutocompleteSupportFragment.OnPlaceSelectedListener() {
//            @Override
//            public void onPlaceSelected(@NonNull AutocompletePlace place) {
//                // Handle the selected place
//                LatLng latLng = place.getLatLng();
//                // Do something with the LatLng, for example, add a marker to the map
//            }
//
//            @Override
//            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
//                // Handle errors during the autocomplete search
//            }
//        });
//    }

}
