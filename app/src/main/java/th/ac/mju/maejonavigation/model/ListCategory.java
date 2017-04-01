package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teh on 3/20/2017.
 */

public class ListCategory {
    @SerializedName("Category")
    List<Category> listCategory;

    public List<Category> getListCategory() {
        return listCategory;
    }

}
