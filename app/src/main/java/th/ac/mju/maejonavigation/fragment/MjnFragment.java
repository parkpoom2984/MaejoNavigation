package th.ac.mju.maejonavigation.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.squareup.otto.Bus;

import io.realm.Realm;


/**
 * Created by Teh on 2/14/2017.
 */

public abstract class MjnFragment extends Fragment{
    private static Bus bus = new Bus();
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

    public static Bus getBus(){
        return bus;
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
