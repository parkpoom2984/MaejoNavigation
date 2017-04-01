package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/18/2017.
 */

public class Floor extends RealmObject{
    @PrimaryKey
    @SerializedName("floorID") int floorId;
    @SerializedName("floorName") String floorName;
    @SerializedName("listRoom")
    RealmList<Room> listRoom;

    public String getFloorName() {
        return floorName;
    }

    public List<Room> getListRoom() {
        return listRoom;
    }

    public int getFloorId() {

        return floorId;
    }
}
