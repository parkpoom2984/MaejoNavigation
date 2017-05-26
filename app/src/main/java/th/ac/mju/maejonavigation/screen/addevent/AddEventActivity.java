package th.ac.mju.maejonavigation.screen.addevent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.model.AddEvent;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.Locations;

public class AddEventActivity extends MjnActivity {
    @InjectView(R.id.event_date_editText)
    TextView eventDate;
    @InjectView(R.id.event_title_editText) TextView eventTitle;
    @InjectView(R.id.event_location_spinner)
    Spinner locationSelect;
    @InjectView(R.id.event_detail_editText)
    AutoCompleteTextView eventDetail;
    @InjectView(R.id.add_event_toolbar)
    Toolbar toolbar;
    private int id;
    private List<Locations> values;

    int year_x, month_x, day_x;
    int cDay, cMonth, cYear;
    static final int DILOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.inject(this);
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.mjn_while));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.ic_close_white));
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                values = realm.where(Locations.class).findAll();
                setUpSpinner();
            }
        });
    }


    @OnClick(R.id.button_add_event)
    public void onClickAddEvent(){
        if (eventTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter event name", Toast.LENGTH_SHORT).show();
            eventTitle.requestFocus();
            return;
        } else if (eventDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter event date", Toast.LENGTH_SHORT).show();
            eventDate.requestFocus();
            return;
        } else if (eventDetail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter event detail", Toast.LENGTH_SHORT).show();
            eventDetail.requestFocus();
            return;
        } else {
            for (int i = 0; i < values.size(); i++) {
                if (locationSelect.getSelectedItem().toString().equals(
                        values.get(i).getLocationName())) {
                    id = values.get(i).getLocationId();
                    break;
                }
            }
            String title = eventTitle.getText().toString();
            String date = eventDate.getText().toString();
            String detail = eventDetail.getText().toString();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", title);
                jsonObject.put("detail", detail);
                jsonObject.put("date", date);
                jsonObject.put("location", id);
                jsonObject.put("status", 0);
            } catch (JSONException e) {
                e.printStackTrace();
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
                    Snackbar.make(eventDate,"can't request, try again.",Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }


    public void setUpSpinner(){
        String[] list = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            list[i] = values.get(i).getLocationName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        locationSelect.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMouth) {
            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            cDay = localCalendar.get(Calendar.DAY_OF_MONTH);
            cMonth = localCalendar.get(Calendar.MONTH) + 1;
            cYear = localCalendar.get(Calendar.YEAR);

            String selectedYear = "" + cYear;
            String selectedMonth = "" + cMonth;
            String selectedDay = "" + cDay;

            DateFormat df = new SimpleDateFormat("yyy/mm/dd"); //Day and Time present
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMouth;

            //ตรวจสอบวันที่ตามเงื่อนที่กำหนด
            if (year_x == cYear) {
                if (month_x >= cMonth) {
                    if (day_x >= cDay) {
                        Toast.makeText(AddEventActivity.this, year_x + " - " + month_x + " - " + day_x, Toast.LENGTH_SHORT).show();
                        eventDate.setText(year_x + "-" + month_x + "-" + day_x);
                    } else {
                        Toast.makeText(AddEventActivity.this, "Please Enter Date New", Toast.LENGTH_SHORT).show();
                        eventDate.requestFocus();
                        return;
                    }
                } else {
                    Toast.makeText(AddEventActivity.this, "Please Enter Date New", Toast.LENGTH_SHORT).show();
                    eventDate.requestFocus();
                    return;
                }

            } else {
                Toast.makeText(AddEventActivity.this, "Please Enter Date New", Toast.LENGTH_SHORT).show();
                eventDate.requestFocus();
                return;
            }
        }
    };

    @OnClick(R.id.event_date_editText)
    public void onClickDateEditText(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        showDialog(DILOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DILOG_ID)
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        return null;

    }
}
