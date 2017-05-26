package th.ac.mju.maejonavigation.request;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import th.ac.mju.maejonavigation.model.AddEvent;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.Event;
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
    Call<Void> addEvent(@Header("data") JSONObject jsonObject);
}
