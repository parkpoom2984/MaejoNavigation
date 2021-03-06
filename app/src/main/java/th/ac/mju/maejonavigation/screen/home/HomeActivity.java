package th.ac.mju.maejonavigation.screen.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

public class HomeActivity extends MjnActivity implements HomePresenter.View{
    private static final int time = 1000;
    private Handler handler;
    private Runnable runnable;
    @InjectView(R.id.home_logo)
    ImageView logoImageView;
    @InjectView(R.id.home_about_us)
    TextView aboutUsTextView;
    private ProgressDialog progressDialog;
    private HomePresenter homePresenter;

    @Override
    public void requestSuccess() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fab_fade_in,R.anim.fab_fade_out);
    }

    @Override
    public void requestFail() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fab_fade_in,R.anim.fab_fade_out);
    }

    private enum State {
        LOADING,
        LOADED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        progressDialog = new ProgressDialog(this);
        updateStatusBar();
        homePresenter = new HomePresenter();
        homePresenter.create(this,getApplicationContext());
        handler = new Handler();
        checkNetworkAvailable();
    }

    private void connectLoadServiceData(){
        runnable = new Runnable() {
            @Override
            public void run()
            {
                if(!homePresenter.isStatusSet() || getRealm().isEmpty()){
                    setState(State.LOADING);
                    homePresenter.callFromService(getService(),getRealm());
                }else{
                    setState(State.LOADING);
                    homePresenter.isLastVersion(getService(),getRealm());
                }
            }
        };
        handler.postDelayed(runnable,time);
    }


    private void updateStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources()
                    .getColor(R.color.mjn_primary));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void setState(State state) {
        switch (state) {
            case LOADING:
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
                logoImageView.setVisibility(View.GONE);
                aboutUsTextView.setVisibility(View.GONE);
                break;
            case LOADED:
                progressDialog.dismiss();
                break;
        }
    }

    public void checkNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Snackbar snackbar = Snackbar.make(logoImageView,R.string.internet_can_not_connect,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.try_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAvailable();
            }
        });
        if(!isConnect){
            snackbar.show();
        }else{
            snackbar.dismiss();
            connectLoadServiceData();
        }
    }
}
