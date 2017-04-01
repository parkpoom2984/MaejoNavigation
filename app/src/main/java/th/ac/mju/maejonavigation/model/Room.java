package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 3/20/2017.
 */

public class Room extends RealmObject{

    @PrimaryKey
    @SerializedName("roomID") String roomId;

    @SerializedName("roomName") String roomName;

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

}
