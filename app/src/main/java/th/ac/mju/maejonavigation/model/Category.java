package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/14/2017.
 */


//create table database
public class Category extends RealmObject{

    public Category() {}

    @PrimaryKey
    @SerializedName("CategoryID")
    int categoryId;

    @SerializedName("CategoryName")
    String categoryName;

    @SerializedName("listLocation")
    RealmList<Locations> listLocation = new RealmList<>();

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public RealmList<Locations> getListLocation() {
        return listLocation;
    }
}
