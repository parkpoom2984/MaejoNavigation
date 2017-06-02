package th.ac.mju.maejonavigation.screen.addevent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.addmarker.AddEventMarkerMapActivity;
import th.ac.mju.maejonavigation.unity.SettingValues;

import static android.view.View.GONE;
import static th.ac.mju.maejonavigation.intent.AddEventIntent.LAT;
import static th.ac.mju.maejonavigation.intent.AddEventIntent.LNG;

public class AddEventActivity extends MjnActivity
        implements OnMapReadyCallback {
    @InjectView(R.id.event_title_editText)
    TextView eventTitle;
    @InjectView(R.id.event_location_spinner)
    Spinner locationSelect;
    @InjectView(R.id.add_event_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.event_list_checkbox)
    CheckBox addEventSelectListCheckbox;
    @InjectView(R.id.event_map_checkbox)
    CheckBox addEventSelectMapCheckbox;
    @InjectView(R.id.add_event_date_end)
    TextView dateEndEvent;
    @InjectView(R.id.add_event_date_start)
    TextView dateStartEvent;
    @InjectView(R.id.event_detail)
    EditText detailEvent;
    @InjectView(R.id.click_here)
    TextView addEventClickHere;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private Calendar calendar;
    private DatePickerDialog datePicker;
    public int REQUEST_CODE = 15;
    private List<Locations> values;
    private DateSelect stateDate;
    private double lat, lng;
    private Date dateStart;
    private Date dateEnd;
    private enum DateSelect {
        START, END
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.inject(this);
        calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.mjn_while));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(
                ContextCompat.getDrawable(this, R.drawable.ic_close_white));
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                values = realm.where(Locations.class).notEqualTo("categoryId", 1)
                        .notEqualTo("categoryId", 4)
                        .notEqualTo("locationId", 1)
                        .notEqualTo("locationId", 24)
                        .findAll();
                setUpSpinner();
            }
        });

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map);
        supportMapFragment.getMapAsync(this);


        //เมื่อเลือก checkbox ที่แสดงรายการ
        locationSelect.setVisibility(GONE); //ซ่อน spinner จากหน้าจอ
        supportMapFragment.getView().setVisibility(GONE);
        addEventSelectListCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEventSelectListCheckbox.isChecked()) { //เมื่อเลือก checkbox แสดงรายการ
                    addEventSelectMapCheckbox.setChecked(false);
                    locationSelect.setVisibility(
                            View.VISIBLE); //แสดง spinner เมื่อเลือก checkbox นี้
                    supportMapFragment.getView().setVisibility(
                            GONE);  //ซ่อนแผนที่ เมื่อเลือก checkbox นี้
                } else {
                    locationSelect.setVisibility(GONE);
                }
            }
        });

        //เมื่อเลือก checkbox ที่แสดงแผนที่
        addEventSelectMapCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEventSelectMapCheckbox.isChecked()) {    //เมื่อเลือก checkbox แสดงแผนที่
                    addEventSelectListCheckbox.setChecked(false);
                    locationSelect.setVisibility(GONE);
                    supportMapFragment.getView().setVisibility(View.VISIBLE);

                    map.getUiSettings().setAllGesturesEnabled(true);
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Intent intent = new Intent(AddEventActivity.this,
                                    AddEventMarkerMapActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                } else {
                    supportMapFragment.getView().setVisibility(GONE);
                }
            }
        });

    }


    @OnClick(R.id.button_add_event)
    public void onClickAddEvent() {
        if (eventTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "please enter event name", Toast.LENGTH_SHORT).show();
        } else if (dateStartEvent.getText().toString().isEmpty()) {
            Toast.makeText(this, "please enter event start date", Toast.LENGTH_SHORT).show();
        } else if (dateEndEvent.getText().toString().isEmpty()) {
            Toast.makeText(this, "please enter event end date", Toast.LENGTH_SHORT).show();
        } else if (detailEvent.getText().toString().isEmpty()) {
            Toast.makeText(this, "please enter event detail", Toast.LENGTH_SHORT).show();
        } else if (!addEventSelectListCheckbox.isChecked() &&
                !addEventSelectMapCheckbox.isChecked()) {
            Toast.makeText(this, "please select event location", Toast.LENGTH_SHORT).show();
        } else if (addEventSelectMapCheckbox.isChecked() && lat == 0 && lng == 0) {
            Toast.makeText(this, "please choose event location on map", Toast.LENGTH_SHORT).show();
        } else {
            String title = eventTitle.getText().toString();
            String startDate = dateStartEvent.getText().toString();
            String endDate = dateEndEvent.getText().toString();
            String detail = detailEvent.getText().toString();
            JSONObject jsonObject = new JSONObject();
            if (addEventSelectListCheckbox.isChecked()) {
                for (Locations locations : values) {
                    if (locationSelect.getSelectedItem().toString().equals(
                            locations.getLocationName())) {
                        int id = locations.getLocationId();
                        try {
                            jsonObject.put("name", title);
                            jsonObject.put("detail", detail);
                            jsonObject.put("date", startDate);
                            jsonObject.put("location", id);
                            jsonObject.put("status", 0);
                            jsonObject.put("eventEndDate", endDate);
                            jsonObject.put("lat", 0);
                            jsonObject.put("lng", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    jsonObject.put("name", title);
                    jsonObject.put("detail", detail);
                    jsonObject.put("date", startDate);
                    jsonObject.put("location", 0);
                    jsonObject.put("status", 0);
                    jsonObject.put("eventEndDate", endDate);
                    jsonObject.put("lat", lat);
                    jsonObject.put("lng", lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Call<Void> callListCategory = getService().addEvent(jsonObject);
            callListCategory.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Snackbar.make(toolbar, "can't request, try again.", Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }
    }


    public void setUpSpinner() {
        String[] list = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            list[i] = values.get(i).getLocationName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        locationSelect.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendarSelect = Calendar.getInstance();
                    calendarSelect.set(year,month,dayOfMonth);
                    Date date = calendarSelect.getTime();
                    String textDate = dateFormat.format(date);
                    if (year >= calendar.get(Calendar.YEAR)) {
                        if (month > calendar.get(Calendar.MONTH)) {
                            if (stateDate == DateSelect.START) {
                                dateStartEvent.setText(textDate);
                                dateStart = date;
                            } else {
                                if(date.after(dateStart)){
                                    dateEndEvent.setText(textDate);
                                }else{
                                    Toast.makeText(AddEventActivity.this, "please select end date after start",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } else if (month == calendar.get(Calendar.MONTH)) {
                            if (dayOfMonth >= calendar.get(Calendar.DAY_OF_MONTH)) {
                                if (stateDate == DateSelect.START) {
                                    dateStartEvent.setText(textDate);
                                    dateStart = date;
                                } else {
                                    if(date.after(dateStart)){
                                        dateEndEvent.setText(textDate);
                                        dateEnd = date;
                                    }else{
                                        Toast.makeText(AddEventActivity.this, "please select end date after start",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(AddEventActivity.this, "please select new date",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(AddEventActivity.this, "please select new date",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(AddEventActivity.this, "please select new date",
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

    @OnClick(R.id.add_event_date_start)
    public void onClickDateStart() {
        stateDate = DateSelect.START;
        datePicker.show();
    }

    @OnClick(R.id.add_event_date_end)
    public void onClickDateEnd() {
        if(dateStartEvent.getText().toString().isEmpty()){
            Toast.makeText(AddEventActivity.this, "please select start date first",
                    Toast.LENGTH_LONG).show();
        }else{
            stateDate = DateSelect.END;
            datePicker.show();
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
        map.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                map.clear();
                lat = data.getDoubleExtra(LAT, 0);
                lng = data.getDoubleExtra(LNG, 0);
                addEventClickHere.setVisibility(View.GONE);
                LatLng latLng = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions()
                        .position(latLng));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                Snackbar.make(eventTitle, "Add location already", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}