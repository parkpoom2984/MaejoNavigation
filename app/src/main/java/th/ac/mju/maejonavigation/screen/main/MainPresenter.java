package th.ac.mju.maejonavigation.screen.main;

import android.util.Log;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.event.SearchEvent;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.model.Room;

/**
 * Created by Teh on 3/31/2017.
 */

public class MainPresenter {
    private SearchListener listener;
    private static final String FIELD_LOCATION_NAME = "locationName";
    private static final String FIELD_ROOM_NAME = "listFloor.listRoom.roomName";

    public void create(SearchListener listener){
        this.listener = listener;
    }

    public void querySearch(Realm realm,final String textSearch){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Location> results = realm.where(Location.class).contains(
                        FIELD_LOCATION_NAME, textSearch).or().equalTo(FIELD_ROOM_NAME,textSearch)
                        .findAllAsync();

                //RealmResults<Location> results = realm.where(Location.class).equalTo(FIELD_ROOM_NAME,textSearch)
                //        .findAll();
                //results.where().equalTo(FIELD_ROOM_NAME,textSearch);
                List<Location> listLocation = realm.copyFromRealm(results);
                listener.updateFromSearch(listLocation);
            }
        });
    }
    interface SearchListener{
        void updateFromSearch(List<Location> listLocation);
    }
}
