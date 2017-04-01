package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Teh on 2/14/2017.
 */



public class Category extends RealmObject{

    public Category() {}

    @PrimaryKey
    @SerializedName("CategoryID")
    int categoryId;

    @SerializedName("CategoryName")
    String categoryName;

    @SerializedName("listLocation")
    RealmList<Location> listLocation;

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public RealmList<Location> getListLocation() {
        return listLocation;
    }
}
