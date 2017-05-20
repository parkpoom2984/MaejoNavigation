package th.ac.mju.maejonavigation.event;

import java.util.List;

import th.ac.mju.maejonavigation.model.Locations;

/**
 * Created by Teh on 3/26/2017.
 */


public class SearchEvent {
    private List<Locations> listLocation;

    public List<Locations> getListLocation() {
        return listLocation;
    }

    public SearchEvent(List<Locations>  listLocation) {
        this.listLocation = listLocation;
    }
}
