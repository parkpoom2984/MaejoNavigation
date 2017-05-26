package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teh on 5/24/2017.
 */

public class ListEvent {
    @SerializedName("listEvent")
    List<Event> listEvent;

    public List<Event> getListEvent() {
        return listEvent;
    }
}
