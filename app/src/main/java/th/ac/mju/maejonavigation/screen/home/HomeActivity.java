package th.ac.mju.maejonavigation.screen.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

    }

    private enum State {
        LOADING,
        FAILURE,
        SUCCESS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        updateStatusBar();
        homePresenter = new HomePresenter();
        homePresenter.create(this,getApplicationContext());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run()
            {
                if(!homePresenter.isStatusSet()){
                    setState(State.LOADING);
                    homePresenter.callFromService(getService(),getRealm());
                }else{
                    setState(State.LOADING);
                    homePresenter.isLastVersion(getService(),getRealm());
                }
            }
        };
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
        handler.postDelayed(runnable,time);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void setState(State state) {
        switch (state) {
            case LOADING:
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("loading");
                progressDialog.show();
                logoImageView.setVisibility(View.GONE);
                aboutUsTextView.setVisibility(View.GONE);
                break;
            case FAILURE:
                break;
        }
    }
}
