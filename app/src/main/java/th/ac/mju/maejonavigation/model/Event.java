package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teh on 5/24/2017.
 */

public class Event {
    @SerializedName("eventID")
    int eventId;
    @SerializedName("eventName")
    String eventName;
    @SerializedName("eventDetail")
    String eventDetail;
    @SerializedName("eventDate")
    String eventDate;
    @SerializedName("locationID")
    int locationId;
    @SerializedName("status")
    int status;

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public String getEventDate() {
        return eventDate;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getStatus() {
        return status;
    }
}
