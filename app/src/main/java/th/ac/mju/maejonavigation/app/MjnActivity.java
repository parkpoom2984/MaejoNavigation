package th.ac.mju.maejonavigation.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.prefer.StringPreference;
import th.ac.mju.maejonavigation.request.MjnApi;

/**
 * Created by Teh on 2/19/2017.
 */

public class MjnActivity extends AppCompatActivity{
    private static Bus bus = new Bus();

    private static final String MJN_SHARED_PREFERENCES = "mjn_shared_preference";
    private static final String DATA_STATUS = "data_status";
    private static StringPreference stringPreference = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(MJN_SHARED_PREFERENCES, MODE_PRIVATE);
        stringPreference = new StringPreference(sp, DATA_STATUS);
        initRealm();
        initFabric();
        //add Jenkins for build .apk
    }

    private void initRealm() {
        Realm.init(this);
    }

    private void initFabric(){
        Fabric.with(this, new Crashlytics());
    }

    public static Bus getBus(){
        return bus;
    }

    public static Realm getRealm(){
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            return Realm.getInstance(config);
    }

    public static MjnApi getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.MJN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MjnApi.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }
}
