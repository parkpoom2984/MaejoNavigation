package th.ac.mju.maejonavigation.screen.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.dialog.SelectCategoryDialog;
import th.ac.mju.maejonavigation.intent.MainIntent;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.ListEvent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.unity.SettingValues;

import static th.ac.mju.maejonavigation.intent.MapIntent.LOCATION_DIRECTION;

public class MapActivity extends MjnActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SelectCategoryDialog.View {
    private SupportMapFragment supportMapFragment;
    private GoogleApiClient googleApiClient;
    private LatLngBounds.Builder builder;
    private GoogleMap map;
    private Marker markerCurrent;
    private LatLng latLngCurrentLocation;
    private boolean isTypeAllLocation;
    private Locations locationDirection;
    private List<Locations> listAllLocation;
    @InjectView(R.id.refresh_map)
    FloatingActionButton refreshMapFloatAction;
    @InjectView(R.id.adView)
    AdView adView;
    @InjectView(R.id.select_map)
    FloatingActionButton selectMapFloatAction;
    AlertDialog alert;
    SelectCategoryDialog selectCategoryDialog;
    private List<Event> listEvent;
    private static final String LOCATION_ID_FILED = "locationId";

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.inject(this);
        if (isNetworkAvailable()) {
            Call<ListEvent> callListEvent = getService().getListEvent();
            callListEvent.enqueue(new Callback<ListEvent>() {
                @Override
                public void onResponse(Call<ListEvent> call, Response<ListEvent> response) {
                    List<Event> value = response.body().getListEvent();
                    listEvent = new ArrayList<>();
                    for (Event event : value) {
                        if (event.getStatus() == 1) {
                            listEvent.add(event);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListEvent> call, Throwable t) {

                }
            });
        } else {
            showSettingAlert();
        }
        initAd();
        isTypeAllLocation = getIntent().getBooleanExtra(MapIntent.TYPE_ALL_LOCATION, false);
        locationDirection = Parcels.unwrap(this.getIntent().getParcelableExtra(LOCATION_DIRECTION));
        selectCategoryDialog = new SelectCategoryDialog(this, this);
        updateAllLocation();
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
                RealmResults<Category> results = realm.where(Category.class).findAll();
                List<Category> listCategory = realm.copyFromRealm(results);
                selectCategoryDialog.create(listCategory);
                alert = selectCategoryDialog.getBuilder().create();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setCompassEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(SettingValues.getLatLngMaejo())
                .zoom(15)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                setupMap();
            }
        } else {
            setupMap();
        }


    }

    @SuppressLint("MissingPermission")
    private void setupMap() {
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable() && isNetworkAvailable()) {
            refreshMapFloatAction.setVisibility(View.VISIBLE);
            selectMapFloatAction.setVisibility(View.VISIBLE);
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest, this);
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLngCurrentLocation = new LatLng(lat, lng);
            if (isTypeAllLocation) {
                refreshMapFloatAction.setVisibility(View.GONE);
            } else {
                refreshMapFloatAction.setVisibility(View.VISIBLE);
                makePolylineOptions(locationDirection);
                setOnClickInfoMarker(null);
            }
        } else {
            showSettingAlert();
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
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latLngCurrentLocation = new LatLng(lat, lng);
        if (markerCurrent != null) {
            markerCurrent.remove();
        }
        int id = getResources().getIdentifier(SettingValues.SELF_MARKER, "drawable",
                getPackageName());
        markerCurrent = map.addMarker(new MarkerOptions().title("ตำแหน่งปัจจุบัน")
                .position(latLngCurrentLocation)
                .icon(
                        BitmapDescriptorFactory.fromResource(id)));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void makePolylineOptions(final Locations location) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        String serverKey = getString(R.string.server_key);
        final LatLng destination = new LatLng(location.getLatitude(), location.getLongitude());
        GoogleDirection.withServerKey(serverKey)
                .from(latLngCurrentLocation)
                .to(destination)
                .alternativeRoute(true)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.getRouteList().size() != 0 &&
                                direction.getRouteList() != null) {
                            if (markerCurrent != null) {
                                markerCurrent.remove();
                            }
                            int selfMarker = getResources().getIdentifier(SettingValues.SELF_MARKER,
                                    "drawable",
                                    getPackageName());
                            markerCurrent = map.addMarker(new MarkerOptions().title(
                                    "ตำแหน่งปัจจุบัน")
                                    .position(latLngCurrentLocation)
                                    .icon(
                                            BitmapDescriptorFactory.fromResource(selfMarker)));
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(
                                    MapActivity.this, pointList, 5,
                                    Color.rgb(67, 160, 71));
                            PolylineOptions start = new PolylineOptions().add(pointList.get(0))
                                    .add(latLngCurrentLocation)
                                    .width(6)
                                    .color(Color.rgb(67, 160, 71))
                                    .geodesic(true);
                            PolylineOptions end = new PolylineOptions().add(
                                    pointList.get(pointList.size() - 1))
                                    .add(destination)
                                    .width(6)
                                    .color(Color.rgb(67, 160, 71))
                                    .geodesic(true);
                            map.addPolyline(polylineOptions);
                            map.addPolyline(start);
                            map.addPolyline(end);

                            int categoryID = location.getCategoryId();

                            MarkerOptions marker = new MarkerOptions().position(destination).title(
                                    location.getLocationName() + "");
                            if (location.getCategoryId() != 0) {
                                int id = getResources().getIdentifier(
                                        SettingValues.CATEGORY_MARKER + categoryID, "drawable",
                                        getPackageName());
                                marker.icon(BitmapDescriptorFactory.fromResource(id));
                            } else {
                                BitmapDescriptor iconEvent = BitmapDescriptorFactory.fromResource(
                                        R.drawable.marker_event);
                                marker.icon(iconEvent);
                            }
                            map.addMarker(marker).showInfoWindow();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();
                            Snackbar.make(refreshMapFloatAction, "ห่างจากสถานที่นี้เป็นระยะทาง " +
                                    distance +
                                    System.getProperty("line.separator") +
                                    "ระยะเวลา " +
                                    duration, Snackbar.LENGTH_LONG).show();
                            progressDialog.cancel();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(latLngCurrentLocation);
                            builder.include(destination);
                            LatLngBounds bounds = builder.build();

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                            map.animateCamera(cu);
                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(refreshMapFloatAction,
                                    "Don't have direction from your location",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });

    }


    private void showSettingAlert() {
        refreshMapFloatAction.setVisibility(View.GONE);
        selectMapFloatAction.setVisibility(View.GONE);
        Snackbar.make(refreshMapFloatAction, "please check Permission and Internet",
                Snackbar.LENGTH_INDEFINITE).setAction(
                "Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).show();
    }

    @OnClick(R.id.refresh_map)
    public void onClickRefresh() {
        map.clear();
        makePolylineOptions(locationDirection);
    }


    @OnClick(R.id.map_menu)
    public void onClickMenu() {
        finish();
    }


    public void setOnClickInfoMarker(final List<Locations> listLocation) {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerName = marker.getTitle();
                if (isTypeAllLocation) {
                    for (Locations locations : listLocation) {
                        if (markerName.equals(locations.getLocationName())) {
                            finish();
                            startActivity(new MainIntent(getApplicationContext(),
                                    locations.getLocationId()));
                            break;
                        }
                    }
                } else {
                    if (!locationDirection.getIsEventLocation()) {
                        finish();
                        startActivity(new MainIntent(getApplicationContext(),
                                locationDirection.getLocationId()));
                    }
                }

            }
        });
    }


    public void setOnClickMarker(final List<Locations> listLocation) {
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
                            selectCategoryDialog.removeListPosition();
                            break;
                        }
                    }
                    for (final Event event : listEvent) {
                        if (markerName.equals(event.getEventName())) {
                            locationDirection = new Locations();
                            locationDirection.setIsEventLocation(true);
                            locationDirection.setLocationName(event.getEventName());
                            if (event.getLat() == 0 && event.getLng() == 0) {
                                getRealm().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Locations location = realm.where(Locations.class)
                                                .equalTo(LOCATION_ID_FILED, event.getLocationId())
                                                .findFirst();
                                        locationDirection.setLongitude(location.getLongitude());
                                        locationDirection.setLatitude(location.getLatitude());
                                    }
                                });
                            } else {
                                locationDirection.setLongitude(event.getLng());
                                locationDirection.setLatitude(event.getLat());
                            }
                            refreshMapFloatAction.setVisibility(View.VISIBLE);
                            refreshMapFloatAction.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onClickRefresh();
                                }
                            });
                            makePolylineOptions(locationDirection);
                            selectCategoryDialog.removeListPosition();
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }


    public void initAd() {
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        AdRequest adRequest = adBuilder.build();
        adView.loadAd(adRequest);
    }

    @OnClick(R.id.select_map)
    public void onClickSelect() {
        alert.show();
    }

    @Override
    public void onClickPositiveButton(ArrayList<Integer> listPositionCategory) {
        builder = new LatLngBounds.Builder();
        map.clear();
        refreshMapFloatAction.setVisibility(View.GONE);
        listAllLocation = new ArrayList<>();
        for (Integer position : listPositionCategory) {
            RealmResults<Locations> listLocation = getRealm().where(Locations.class).equalTo(
                    "categoryId", position).findAll();
            if (listLocation.size() == 0) {
                final BitmapDescriptor iconEvent = BitmapDescriptorFactory.fromResource(
                        R.drawable.marker_event);
                getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Event event : listEvent) {
                            LatLng latLngLocation = null;
                            if (event.getLat() == 0 && event.getLng() == 0) {
                                Locations location = realm.where(Locations.class).equalTo(
                                        LOCATION_ID_FILED, event.getLocationId()).findFirst();
                                latLngLocation = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                            } else {
                                latLngLocation = new LatLng(event.getLat(), event.getLng());
                            }
                            builder.include(latLngLocation);
                            map.addMarker(new MarkerOptions().position(latLngLocation)
                                    .title(event.getEventName() + "")
                                    .icon(iconEvent));
                        }
                    }
                });
            } else {
                for (Locations location : listLocation) {
                    listAllLocation.add(location);
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    LatLng latLngLocation = new LatLng(lat, lng);
                    builder.include(latLngLocation);
                    int id = getResources().getIdentifier(
                            SettingValues.CATEGORY_MARKER + location.getCategoryId(), "drawable",
                            getPackageName());
                    map.addMarker(new MarkerOptions().position(latLngLocation)
                            .title(location.getLocationName() + "")
                            .icon(BitmapDescriptorFactory.fromResource(id)));
                }
            }
        }
        setOnClickMarker(listAllLocation);
        setOnClickInfoMarker(listAllLocation);
        if (!listPositionCategory.isEmpty()) {
            isTypeAllLocation = true;
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
            map.animateCamera(cu);
        }
    }

    @Override
    public void onClickNeutralButton() {

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    setupMap();
                } else {
                    // Permission Denied
                    showSettingAlert();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
