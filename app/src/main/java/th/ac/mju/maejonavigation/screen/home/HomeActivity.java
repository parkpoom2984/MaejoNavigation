package th.ac.mju.maejonavigation.screen.home;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

public class HomeActivity extends AppCompatActivity {

    private static final int time = 1000;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        updateStatusBar();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run()
            {
                finish();
                if(isFinishing()){
                    //if (getAuthTokenManager().isLoggedIn()) {
                    //    DashBoardActivity.startLogin(getApplicationContext());
                    //}else{
                    //    startActivity(new Intent(HomeActivity.this,WelcomeActivity.class));
                    //}

                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fab_fade_in,R.anim.fab_fade_out);
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
}
