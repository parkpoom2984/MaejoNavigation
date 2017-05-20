package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;


import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;


import io.realm.FloorRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/18/2017.
 */
@Parcel(implementations = { FloorRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Floor.class })
public class Floor extends RealmObject {
    @PrimaryKey
    @SerializedName("floorID") int floorId;
    @SerializedName("floorName") String floorName;
    @SerializedName("imagePlanPath") String imagePlanPath;
    @SerializedName("listRoom")
    RealmList<Room> listRoom = new RealmList<>();

    public String getFloorName() {
        return floorName;
    }

    public List<Room> getListRoom() {
        return listRoom;
    }

    public int getFloorId() {

        return floorId;
    }
    public String getImagePlanPath() {
        return imagePlanPath;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public void setImagePlanPath(String imagePlanPath) {
        this.imagePlanPath = imagePlanPath;
    }

    //public void setListRoom(RealmList<Room> listRoom) {
    //    this.listRoom = listRoom;
    //}
}
