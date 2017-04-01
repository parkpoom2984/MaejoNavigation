package th.ac.mju.maejonavigation.event;

import th.ac.mju.maejonavigation.model.Location;

/**
 * Created by Teh on 3/26/2017.
 */

public class SelectLocationEvent {
    private Location location;

    public SelectLocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
