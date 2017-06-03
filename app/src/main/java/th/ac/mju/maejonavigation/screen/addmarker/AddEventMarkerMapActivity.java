package th.ac.mju.maejonavigation.screen.addmarker;


import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.intent.AddEventIntent;
import th.ac.mju.maejonavigation.unity.SettingValues;

public class AddEventMarkerMapActivity extends MjnActivity implements OnMapReadyCallback {


    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private Marker markerEvent;
    private LatLng newLatLng;

    @InjectView(R.id.add_map_toolbar) Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_marker_map);
        ButterKnife.inject(this);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.mjn_while));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_white));
        supportMapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);

    }

    @OnClick(R.id.button_add_event_marker_map)
    public void onClickAddEventLocationMarker() {
        if(newLatLng != null){
            setResult(Activity.RESULT_OK, new AddEventIntent(this,newLatLng.latitude,newLatLng.longitude));
            finish();
        }else{
            Toast.makeText(this,"please choose location on the map first",Toast.LENGTH_LONG).show();
        }

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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (markerEvent != null) {
                    map.clear();
                }
                newLatLng = new LatLng(point.latitude, point.longitude);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(newLatLng);
                markerEvent = map.addMarker(markerOptions);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
