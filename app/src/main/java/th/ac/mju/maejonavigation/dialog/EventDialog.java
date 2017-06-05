package th.ac.mju.maejonavigation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import io.realm.Realm;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.Locations;

/**
 * Created by Teh on 5/12/2017.
 */

public class EventDialog {
    private Dialog dialog;
    private Event event;
    private TextView titleEvent;
    private TextView dateEvent;
    private TextView detailEvent;
    private TextView locationEvent;
    private static final String LOCATION_ID_FILED = "locationId";

    public EventDialog(final Context context, final Event event, Realm realm) {
        this.event = event;
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(false);
        TextView closeIcon = (TextView) dialog.findViewById(R.id.dialog_event_close);
        final TextView goToMapIcon = (TextView) dialog.findViewById(R.id.dialog_event_go_to_map);
        titleEvent = (TextView) dialog.findViewById(R.id.dialog_event_title);
        dateEvent = (TextView) dialog.findViewById(R.id.dialog_event_date);
        detailEvent = (TextView) dialog.findViewById(R.id.dialog_event_detail);
        locationEvent = (TextView) dialog.findViewById(R.id.dialog_event_location);
        onClickMap(goToMapIcon);
        onClickClose(closeIcon);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Locations location = new Locations();
                if (event.getLocationId() == 0) {
                    location.setIsEventLocation(true);
                    location.setLocationName(event.getEventName());
                    location.setLongitude(event.getLng());
                    location.setLatitude(event.getLat());
                    updateUI("กำหนดพิกัดผ่านแผนที่");
                } else {
                    Locations locationEvent = realm.where(Locations.class).equalTo(
                            LOCATION_ID_FILED, event.getLocationId()).findFirst();
                    location.setIsEventLocation(true);
                    location.setLocationName(event.getEventName());
                    location.setLongitude(locationEvent.getLongitude());
                    location.setLatitude(locationEvent.getLatitude());
                    updateUI(locationEvent.getLocationName());
                }
                goToMapIcon.setTag(location);
            }
        });

    }

    public void show() {
        dialog.show();
    }

    public void onClickMap(TextView goToMapTextView) {
        goToMapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getContext().startActivity(
                        new MapIntent(dialog.getContext(), false, (Locations) view.getTag()));
            }
        });
    }

    public void onClickClose(TextView closeTextView) {
        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void updateUI(String locationName) {
        Context context = dialog.getContext();
        titleEvent.setText(event.getEventName());
        locationEvent.setText(
                context.getResources().getString(R.string.event_location, locationName));
        dateEvent.setText(context.getResources()
                .getString(R.string.event_date, event.getEventStartDate(),
                        event.getEventEndDate()));
        detailEvent.setText(
                context.getResources().getString(R.string.event_detail, event.getEventDetail()));
    }
}
