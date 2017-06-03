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
    String eventStartDate;
    @SerializedName("locationID")
    int locationId;
    @SerializedName("status")
    int status;
    @SerializedName("eventEndDate")
    String eventEndDate;
    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getStatus() {
        return status;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
