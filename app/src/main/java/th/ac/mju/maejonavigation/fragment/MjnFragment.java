package th.ac.mju.maejonavigation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squareup.otto.Bus;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.request.MjnApi;


/**
 * Created by Teh on 2/14/2017.
 */

public abstract class MjnFragment extends Fragment {
    private static Bus bus = new Bus();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealm();
    }

    private void initRealm() {
        Realm.init(getContext());
    }


    public static Realm getRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(config);
    }

    public static Bus getBus() {
        return bus;
    }

    public static MjnApi getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.MJN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MjnApi.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }
}
