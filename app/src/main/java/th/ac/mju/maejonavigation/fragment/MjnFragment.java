package th.ac.mju.maejonavigation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.mju.maejonavigation.BuildConfig;
import th.ac.mju.maejonavigation.request.MjnApi;


/**
 * Created by Teh on 2/14/2017.
 */

public abstract class MjnFragment extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealm();
    }

    private void initRealm(){
        Realm.init(getContext());
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

}
