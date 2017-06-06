package th.ac.mju.maejonavigation.request;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.ListEvent;


/**
 * Created by Teh on 2/10/2017.
 */

public interface  MjnApi {
    @GET("JSONDataServlet")
    Call<ListCategory> getListCategory();
    @GET("JSONStatusServlet")
    Call<DataStatus> getDataStatus();
    @GET("JSONEventServlet")
    Call<ListEvent> getListEvent();
    @POST("JSONEventServlet")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> addEvent(@Header("data") JSONObject jsonObject);
}
