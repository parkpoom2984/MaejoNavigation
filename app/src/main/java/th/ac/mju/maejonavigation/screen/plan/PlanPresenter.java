package th.ac.mju.maejonavigation.screen.plan;

import java.util.List;

import io.realm.Realm;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Room;

/**
 * Created by Teh on 5/12/2017.
 */

public class PlanPresenter {

    private View listener;
    private static final String FLOOR_ID = "floorId";

    public PlanPresenter(View listener) {
        this.listener = listener;
    }

    public void getListRoom(Realm realm, final int floorId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Floor floor = realm.where(Floor.class).equalTo(FLOOR_ID, floorId).findFirst();
                listener.showListRoom(floor.getListRoom());
            }
        });
    }

    interface View {
        void showListRoom(List<Room> listRoom);
    }
}
