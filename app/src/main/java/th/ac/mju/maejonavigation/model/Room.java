package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.RoomRealmProxy;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 3/20/2017.
 */
@Parcel(implementations = { RoomRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Room.class })
public class Room extends RealmObject{

    @PrimaryKey
    @SerializedName("roomID") int roomId;

    @SerializedName("roomName") String roomName;
    @SerializedName("width") int width;
    @SerializedName("high") int high;
    @SerializedName("roomType") String roomType;
    @SerializedName("roomDetails") String roomDetail;
    @SerializedName("capacity") int roomCapacity;

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getWidth() {
        return width;
    }

    public int getHigh() {
        return high;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomDetail() {
        return roomDetail;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }
}
