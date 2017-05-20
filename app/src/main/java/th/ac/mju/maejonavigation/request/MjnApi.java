package th.ac.mju.maejonavigation.request;

import retrofit2.Call;
import retrofit2.http.GET;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.ListCategory;


/**
 * Created by Teh on 2/10/2017.
 */

public interface  MjnApi {
    @GET("JSONDataServlet")
    Call<ListCategory> getListCategory();
    @GET("JSONStatusServlet")
    Call<DataStatus> getDataStatus();
}
