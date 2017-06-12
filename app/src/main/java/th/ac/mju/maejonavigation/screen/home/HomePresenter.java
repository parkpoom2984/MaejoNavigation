package th.ac.mju.maejonavigation.screen.home;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.prefer.StringPreference;
import th.ac.mju.maejonavigation.request.MjnApi;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Teh on 3/31/2017.
 */

public class HomePresenter {
    private View view;
    private StringPreference stringPreference;
    private StringPreference favoritePreference;
    private static final String MJN_SHARED_PREFERENCES = "mjn_shared_preference";
    private static final String DATA_STATUS = "data_status";
    private static final String LIST_LOCATION_FAVORITE = "location_favorite";
    private static final String FAVORITE_STATUS = "favoriteStatus";

    public void create(View view, Context context) {
        this.view = view;
        SharedPreferences sp = context.getSharedPreferences(MJN_SHARED_PREFERENCES, MODE_PRIVATE);
        stringPreference = new StringPreference(sp, DATA_STATUS);
        favoritePreference = new StringPreference(sp, LIST_LOCATION_FAVORITE);
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
                if (!isLastVersion) {
                    removeData(realm);
                    callFromService(service, realm);
                } else {
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
                Gson gson = new Gson();
                Type type = new TypeToken<List<Locations>>() {}.getType();
                final List<Locations> listFavorite = gson.fromJson(favoritePreference.get(), type);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Category category : listCategory) {
                            //set categoryId into Locations class
                            for (Locations location : category.getListLocation()) {
                                if (listFavorite != null) {
                                    for (Locations favorite : listFavorite) {
                                        if (favorite.getLocationId() == location.getLocationId() &&
                                                favorite.getLocationName().equals(
                                                        location.getLocationName())) {
                                            location.setFavoriteStatus(1);
                                        }
                                    }
                                }
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

            }
        });
        favoritePreference.delete();
    }

    public void removeData(Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Locations> results = realm.where(Locations.class).equalTo(
                        FAVORITE_STATUS, 1).findAll();
                List<Locations> listFavorite = realm.copyFromRealm(results);
                String json = new Gson().toJson(listFavorite);
                favoritePreference.set(json);
                realm.deleteAll();
            }
        });
    }

    interface View {
        void requestSuccess();

        void requestFail();
    }
}
