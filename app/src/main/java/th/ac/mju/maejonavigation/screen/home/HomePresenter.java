package th.ac.mju.maejonavigation.screen.home;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.prefer.BooleanPreference;
import th.ac.mju.maejonavigation.request.MjnApi;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Teh on 3/31/2017.
 */

public class HomePresenter {
    private View view;
    private Context context;
    private BooleanPreference booleanPreference;
    private static final String MJN_SHARED_PREFERENCES = "mjn_shared_preference";
    private static final String FIRST_TIME = "first_time";

    public void create(View view,Context context) {
        this.view = view;
        this.context = context;
    }

    public boolean getPreference(){
        SharedPreferences sp = context.getSharedPreferences(MJN_SHARED_PREFERENCES,MODE_PRIVATE);
        booleanPreference = new BooleanPreference(sp,FIRST_TIME);
        return booleanPreference.get();
    }

    public void callFromService(MjnApi service, final Realm realm) {
        Call<ListCategory> call = service.getListCategory();
        call.enqueue(new Callback<ListCategory>() {
            @Override
            public void onResponse(Call<ListCategory> call, Response<ListCategory> response) {
                final List<Category> listCategory = response.body().getListCategory();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Category category : listCategory) {
                            //set categoryId into Location class
                            for (Location location : category.getListLocation()) {
                                location.setCategoryId(category.getCategoryId());
                            }
                            realm.copyToRealm(category);
                            view.requestSuccess();
                            booleanPreference.set(true);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ListCategory> call, Throwable t) {
                view.requestFail();
            }
        });

    }

    interface View {
        void requestSuccess();

        void requestFail();
    }
}
