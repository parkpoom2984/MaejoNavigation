package th.ac.mju.maejonavigation.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import javax.inject.Inject;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.request.MjnApi;


/**
 * Created by Teh on 2/19/2017.
 */

public class MjnActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealm();
        initFabric();
    }

    private void initRealm() {
        Realm.init(this);
    }

    private void initFabric(){
        Fabric.with(this, new Crashlytics());
    }

    public static Realm getRealm(){
        return Realm.getDefaultInstance();
    }

    public static MjnApi getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.MJN_API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MjnApi.class);
    }

}
