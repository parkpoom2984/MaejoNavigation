package th.ac.mju.maejonavigation.screen.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.prefer.BooleanPreference;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

public class HomeActivity extends MjnActivity implements HomePresenter.View{
    private static final int time = 1000;
    private Handler handler;
    private Runnable runnable;
    @InjectView(R.id.home_logo)
    ImageView logoImageView;
    @InjectView(R.id.home_about_us)
    TextView aboutUsTextView;
    @InjectView(R.id.home_progress_loading)
    TextView progressLoading;

    private HomePresenter homePresenter;

    @Override
    public void requestSuccess() {
        setState(State.SUCCESS);
        intentMain();
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
                if(!homePresenter.getPreference()){
                    setState(State.LOADING);
                    homePresenter.callFromService(getService(),getRealm());
                    //getRealm().commitTransaction();
                }else{
                    intentMain();
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
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void setState(State state) {
        switch (state) {
            case LOADING:
                progressLoading.setVisibility(View.VISIBLE);
                logoImageView.setVisibility(View.GONE);
                aboutUsTextView.setVisibility(View.GONE);
                break;
            case SUCCESS:
                progressLoading.setVisibility(View.GONE);
                break;
            case FAILURE:
                break;
        }
    }

    private void intentMain(){
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fab_fade_in,R.anim.fab_fade_out);
    }
}
