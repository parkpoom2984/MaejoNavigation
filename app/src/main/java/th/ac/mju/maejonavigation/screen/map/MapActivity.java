package th.ac.mju.maejonavigation.screen.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.intent.MainIntent;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.MainActivity;
import th.ac.mju.maejonavigation.unity.SettingValues;

import static th.ac.mju.maejonavigation.intent.MapIntent.LOCATION_DIRECTION;
import static th.ac.mju.maejonavigation.intent.PlanIntent.FLOOR;

public class MapActivity extends MjnActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private SupportMapFragment supportMapFragment;
    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private Marker markerCurrent;
    private LatLng latLngCurrentLocation;
    private boolean isTypeAllLocation;
    private Locations locationDirection;
    @InjectView(R.id.refresh_map) FloatingActionButton refreshMapFloatAction;
    @InjectView(R.id.adView)
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.inject(this);
        initAd();
        isTypeAllLocation = getIntent().getBooleanExtra(MapIntent.TYPE_ALL_LOCATION,false);
        locationDirection = Parcels.unwrap(this.getIntent().getParcelableExtra(LOCATION_DIRECTION));

        supportMapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void updateAllLocation() {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Locations> results = realm.where(Locations.class).findAll();
                List<Locations> listLocation = realm.copyFromRealm(results);
                for(Locations location : listLocation){
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    int id = getResources().getIdentifier(SettingValues.CATEGORY_MARKER+location.getCategoryId(), "drawable", getPackageName());
                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(location.getLocationName() + "").icon(BitmapDescriptorFactory.fromResource(id)));
                }
                setOnClickMarker(listLocation);
                setOnClickInfoMarker(listLocation);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setCompassEnabled(false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLngCurrentLocation = new LatLng(lat,lng);
            if(isTypeAllLocation){
                refreshMapFloatAction.setVisibility(View.GONE);
                updateAllLocation();
            }else {
                refreshMapFloatAction.setVisibility(View.VISIBLE);
                makePolylineOptions(locationDirection);
            }
        } else {
            // Do something when Locations Provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng coordinate = new LatLng(location.getLatitude()
                ,location.getLongitude());
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latLngCurrentLocation = new LatLng(lat,lng);
        if (markerCurrent != null) {
            markerCurrent.remove();
        }
        int id = getResources().getIdentifier(SettingValues.SELF_MARKER, "drawable", getPackageName());
        markerCurrent = map.addMarker(new MarkerOptions().title("ตำแหน่งปัจจุบัน").position(latLngCurrentLocation).icon(
                BitmapDescriptorFactory.fromResource(id)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(coordinate)
                .zoom(17)
                .bearing(90)
                .tilt(30)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void makePolylineOptions(final Locations location){
        final ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        String serverKey = "AIzaSyCkpODGo-CgaB0NF-jqqh3jNSx4dOcbl9o";
        //final LatLng origin = new LatLng(lat,lng);
        final LatLng destination = new LatLng(location.getLatitude(),location.getLongitude());
        GoogleDirection.withServerKey(serverKey)
                .from(latLngCurrentLocation)
                .to(destination)
                .alternativeRoute(true)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        ArrayList<LatLng> pointList = leg.getDirectionPoint();
                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapActivity.this, pointList, 5,
                                Color.rgb(67, 160, 71));
                        PolylineOptions start = new PolylineOptions().add(pointList.get(0)).add(latLngCurrentLocation).width(6).color(Color.rgb(67, 160, 71)).geodesic(true);
                        PolylineOptions end = new PolylineOptions().add(pointList.get(pointList.size() - 1)).add(destination).width(6).color(Color.rgb(67, 160, 71)).geodesic(true);
                        map.addPolyline(polylineOptions);
                        map.addPolyline(start);
                        map.addPolyline(end);

                        int categoryID = location.getCategoryId();
                        int id = getResources().getIdentifier(SettingValues.CATEGORY_MARKER+categoryID, "drawable", getPackageName());
                        map.addMarker(new MarkerOptions().position(destination).title(location.getLocationName() + "").icon(BitmapDescriptorFactory.fromResource(id))).showInfoWindow();

                        Info distanceInfo = leg.getDistance();
                        Info durationInfo = leg.getDuration();
                        String distance = distanceInfo.getText();
                        String duration = durationInfo.getText();
                        Snackbar.make(refreshMapFloatAction, "ห่างจากสถานที่นี้เป็นระยะทาง " + distance + System.getProperty ("line.separator")+"ระยะเวลา " + duration, Snackbar.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }

    @OnClick(R.id.refresh_map)
    public void onClickRefresh(){
        map.clear();
        makePolylineOptions(locationDirection);
    }

    @OnClick(R.id.map_menu)
    public void onClickMenu(){
        startActivity(new Intent(MapActivity.this,MainActivity.class));
    }


    public void setOnClickInfoMarker(final List<Locations> listLocation){
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerName = marker.getTitle();
                if(isTypeAllLocation) {
                    for (Locations locations : listLocation) {
                        if (markerName.equals(locations.getLocationName())) {
                            startActivity(new MainIntent(getApplicationContext(),locations.getLocationId()));
                            break;
                        }
                    }
                }else{
                    startActivity(new MainIntent(getApplicationContext(),locationDirection.getLocationId()));
                }

            }
        });
    }


    public void setOnClickMarker(final List<Locations> listLocation){
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                if (isTypeAllLocation) {
                    map.clear();
                    updateAllLocation();
                    String markerName = marker.getTitle();
                    for (Locations locations : listLocation) {
                        if (markerName.equals(locations.getLocationName())) {
                            locationDirection = locations;
                            refreshMapFloatAction.setVisibility(View.VISIBLE);
                            refreshMapFloatAction.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onClickRefresh();
                                }
                            });
                            makePolylineOptions(locationDirection);
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }

    public void initAd(){
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        AdRequest adRequest = adBuilder.build();
        adView.loadAd(adRequest);
    }
}
