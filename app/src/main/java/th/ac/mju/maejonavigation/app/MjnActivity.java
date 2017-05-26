package th.ac.mju.maejonavigation.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.model.Room;
import th.ac.mju.maejonavigation.request.MjnApi;

/**
 * Created by Teh on 2/19/2017.
 */

public class MjnActivity extends AppCompatActivity{
    private static Bus bus = new Bus();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //RealmConfiguration config = new RealmConfiguration.Builder().build();
        //Realm.setDefaultConfiguration(config);
        initRealm();
        initFabric();
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
        return Realm.getDefaultInstance();
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
