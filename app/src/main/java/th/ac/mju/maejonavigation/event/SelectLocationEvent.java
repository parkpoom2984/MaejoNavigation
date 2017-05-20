package th.ac.mju.maejonavigation.event;

import th.ac.mju.maejonavigation.model.Locations;

/**
 * Created by Teh on 3/26/2017.
 */

public class SelectLocationEvent {
    private Locations location;

    public SelectLocationEvent(Locations location) {
        this.location = location;
    }

    public Locations getLocation() {
        return location;
    }
}
