package th.ac.mju.maejonavigation.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teh on 5/19/2017.
 */
public class DataStatus {
    @SerializedName("statusVersionName")
    String statusVersionName;

    public String getStatusVersionName() {
        return statusVersionName;
    }
}
