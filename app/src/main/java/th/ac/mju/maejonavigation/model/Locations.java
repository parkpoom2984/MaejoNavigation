package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

import io.realm.LocationsRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/14/2017.
 */
@Parcel(implementations = { LocationsRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Locations.class })
public class Locations extends RealmObject implements Serializable{

    @PrimaryKey
    @SerializedName("locationID") int locationId;

    @SerializedName("locationName") String locationName;

    @SerializedName("locationDetails") String locationDetails;

    @SerializedName("imageLocationPath") String imageLocationPath;

    @SerializedName("latitude") double latitude;

    @SerializedName("longitude") double longitude;

    @SerializedName("favoritestatus") int favoriteStatus;

    @SerializedName("listFloor")
    RealmList<Floor> listFloor = new RealmList<>();

    private int categoryId;
    private boolean isEventLocation = false;
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

    public Locations() {

    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public void setImageLocationPath(String imageLocationPath) {
        this.imageLocationPath = imageLocationPath;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void addListFloor(Floor floor) {
        listFloor.add(floor);
    }

    public int getFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(int favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public boolean getIsEventLocation() {
        return isEventLocation;
    }

    public void setIsEventLocation(boolean isEventLocation) {
        this.isEventLocation = isEventLocation;
    }
}