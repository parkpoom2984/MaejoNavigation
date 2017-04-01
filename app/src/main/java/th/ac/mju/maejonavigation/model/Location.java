package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/14/2017.
 */

public class Location extends RealmObject{

    @PrimaryKey
    @SerializedName("locationID") int locationId;

    @SerializedName("locationName") String locationName;

    @SerializedName("locationDetails") String locationDetails;

    @SerializedName("imageLocationPath") String imageLocationPath;

    @SerializedName("latitude") double latitude;

    @SerializedName("longitude") double longitude;

    @SerializedName("listFloor")
    RealmList<Floor> listFloor;

    int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public String getImageLocationPath() {
        return imageLocationPath;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<Floor> getListFloor() {
        return listFloor;
    }

    public Location() {

    }
}