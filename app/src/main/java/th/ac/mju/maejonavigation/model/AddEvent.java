package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teh on 5/25/2017.
 */

public class AddEvent {
    @SerializedName("name")
    String eventName;
    @SerializedName("detail")
    String eventDetail;
    @SerializedName("date")
    String eventDete;
    @SerializedName("location")
    int eventLocation;
    @SerializedName("status")
    int eventStatus;

    public AddEvent(String eventName, String eventDetail, String eventDete, int eventLocation,
            int eventStatus) {
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.eventDete = eventDete;
        this.eventLocation = eventLocation;
        this.eventStatus = eventStatus;
    }
}
