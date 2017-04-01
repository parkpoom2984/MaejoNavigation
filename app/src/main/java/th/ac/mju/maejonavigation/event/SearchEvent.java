package th.ac.mju.maejonavigation.event;

import java.util.List;

import io.realm.RealmResults;
import th.ac.mju.maejonavigation.model.Location;

/**
 * Created by Teh on 3/26/2017.
 */

public class SearchEvent {
    private List<Location> listLocation;

    public List<Location> getListLocation() {
        return listLocation;
    }

    public SearchEvent(List<Location>  listLocation) {
        this.listLocation = listLocation;
    }
}
