package th.ac.mju.maejonavigation.screen.map;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.nitrico.mapviewpager.MapViewPager;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

public class MapActivity extends MjnActivity implements MapViewPager.Callback{
    @InjectView(R.id.viewPager) ViewPager viewPager;
    private MapViewPager mvp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager.setPageMargin(Utils.dp(this, 18));
        Utils.setMargins(viewPager, 0, 0, 0, 0);
        SupportMapFragment map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        RealmResults<Location> results = getRealm().where(Location.class).findAll();
        mvp = new MapViewPager.Builder(this)
                .mapFragment(map)
                .viewPager(viewPager)
                .adapter(new MapAdapter(getSupportFragmentManager(),results))
                .position(2)
                .callback(this)
                .build();
    }

    @Override
    public void onMapViewPagerReady() {
        mvp.getMap().setPadding(
                0,
                Utils.dp(this, 40),
                Utils.getNavigationBarWidth(this),
                viewPager.getHeight() + Utils.getNavigationBarHeight(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_main){
            Intent intent = new Intent(MapActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //overridePendingTransition(R.anim.fab_fade_in,R.anim.fab_fade_out);
        }
        return true;
    }


}
