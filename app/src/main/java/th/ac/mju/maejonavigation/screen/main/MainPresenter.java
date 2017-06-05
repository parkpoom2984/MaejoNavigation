package th.ac.mju.maejonavigation.screen.main;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Locations;

/**
 * Created by Teh on 3/31/2017.
 */

public class MainPresenter {
    private SearchListener listener;

    public void create(SearchListener listener) {
        this.listener = listener;
    }

    public void querySearch(Realm realm, final String textSearch) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Locations> results = realm.where(Locations.class).findAll();
                List<Locations> listLocation = searchByKeyword(textSearch,
                        realm.copyFromRealm(results));
                listener.updateFromSearch(listLocation);
            }
        });
    }

    public void queryLocationFromMap(Realm realm, final int locationId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Locations location = realm.where(Locations.class)
                        .equalTo("locationId", locationId)
                        .findFirst();
                listener.switchToLocationDetail(location);
            }
        });
    }

    public ArrayList<Locations> searchByKeyword(String txt, List<Locations> listLocation) {
        ArrayList<Locations> filter = new ArrayList<Locations>();
        for (int i = 0; i < listLocation.size(); i++) {
            Locations location = new Locations();
            location.setLocationId(listLocation.get(i).getLocationId());
            location.setLocationName(listLocation.get(i).getLocationName());
            location.setLocationDetails(listLocation.get(i).getLocationDetails());
            location.setImageLocationPath(listLocation.get(i).getImageLocationPath());
            location.setLatitude(listLocation.get(i).getLatitude());
            location.setLongitude(listLocation.get(i).getLongitude());
            //location.setFavoriteStatus(listLocation.get(i).getFavoriteStatus());
            location.setCategoryId(listLocation.get(i).getCategoryId());
            if (listLocation.get(i).getLocationName().contains(txt)) {
                filter.add(location);
            }
            ArrayList<Floor> listFloor = new ArrayList<Floor>();
            for (int j = 0; j < listLocation.get(i).getListFloor().size(); j++) {
                Floor floor = new Floor();
                floor.setFloorId(listLocation.get(i).getListFloor().get(j).getFloorId());
                floor.setFloorName(listLocation.get(i).getListFloor().get(j).getFloorName());
                floor.setImagePlanPath(listLocation.get(i)
                        .getListFloor()
                        .get(j)
                        .getImagePlanPath());
                for (int a = 0; a < listLocation.get(i).getListFloor().get(j).getListRoom().size();
                        a++) {
                    if (listLocation.get(i)
                            .getListFloor()
                            .get(j)
                            .getListRoom()
                            .get(a)
                            .getRoomName()
                            .contains(txt)) {

                        floor.getListRoom().add(listLocation.get(i)
                                .getListFloor()
                                .get(j)
                                .getListRoom()
                                .get(a));
                    }
                }
                if (floor.getListRoom().size() != 0) {
                    listFloor.add(floor);
                }
            }

            if (listFloor.size() != 0) {
                for (Floor floor : listFloor) {
                    location.addListFloor(floor);
                }
                filter.add(location);
            }
        }

        return filter;
    }

    interface SearchListener {
        void updateFromSearch(List<Locations> listLocation);

        void switchToLocationDetail(Locations location);
    }
}
