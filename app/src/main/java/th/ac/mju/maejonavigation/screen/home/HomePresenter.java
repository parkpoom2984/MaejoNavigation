package th.ac.mju.maejonavigation.screen.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.model.Room;
import th.ac.mju.maejonavigation.prefer.StringPreference;
import th.ac.mju.maejonavigation.request.MjnApi;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Teh on 3/31/2017.
 */

public class HomePresenter {
    private View view;
    private Context context;
    private StringPreference stringPreference;
    private static final String MJN_SHARED_PREFERENCES = "mjn_shared_preference";
    private static final String DATA_STATUS = "data_status";
    public void create(View view, Context context) {
        this.view = view;
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences(MJN_SHARED_PREFERENCES, MODE_PRIVATE);
        stringPreference = new StringPreference(sp, DATA_STATUS);
    }

    public boolean isStatusSet() {
        return stringPreference.isSet();
    }

    public void isLastVersion(final MjnApi service, final Realm realm) {
        Call<DataStatus> callDataStatus = service.getDataStatus();
        callDataStatus.enqueue(new Callback<DataStatus>() {
            @Override
            public void onResponse(Call<DataStatus> call,
                    Response<DataStatus> response) {
                boolean isLastVersion = response.body().getStatusVersionName().equals(
                        stringPreference.get());
                if(!isLastVersion){
                    removeData(realm);
                    callFromService(service,realm);
                }else{
                    view.requestSuccess();
                }
            }

            @Override
            public void onFailure(Call<DataStatus> call, Throwable t) {
                view.requestFail();
            }
        });
    }

    public void callFromService(final MjnApi service, final Realm realm) {
        Call<ListCategory> callListCategory = service.getListCategory();
        callListCategory.enqueue(new Callback<ListCategory>() {
            @Override
            public void onResponse(Call<ListCategory> call, Response<ListCategory> response) {
                final List<Category> listCategory = response.body().getListCategory();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Category category : listCategory) {
                            //set categoryId into Locations class
                            for (Locations location : category.getListLocation()) {
                                location.setCategoryId(category.getCategoryId());
                            }
                            realm.copyToRealm(category);
                        }
                        Call<DataStatus> callDataStatus = service.getDataStatus();
                        callDataStatus.enqueue(new retrofit2.Callback<DataStatus>() {
                            @Override
                            public void onResponse(Call<DataStatus> call,
                                    Response<DataStatus> response) {
                                stringPreference.set(
                                        response.body().getStatusVersionName());
                                view.requestSuccess();
                            }

                            @Override
                            public void onFailure(Call<DataStatus> call, Throwable t) {
                                view.requestFail();
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<ListCategory> call, Throwable t) {
                view.requestFail();
            }
        });

    }

    public void removeData(Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();

            }
        });
    }

    interface View {
        void requestSuccess();

        void requestFail();
    }
}
