package th.ac.mju.maejonavigation.screen.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import de.greenrobot.event.EventBus;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;

import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.category.CategoryFragment;
import th.ac.mju.maejonavigation.screen.main.detail.DetailFragment;

import th.ac.mju.maejonavigation.screen.main.favorite.FavoriteFragment;
import th.ac.mju.maejonavigation.screen.main.location.LocationFragment;

import static java.security.AccessController.getContext;
import static th.ac.mju.maejonavigation.intent.MainIntent.LOCATION_ID;
import static th.ac.mju.maejonavigation.intent.PlanIntent.LOCATION_NAME;
import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.*;

public class MainActivity extends MjnActivity implements MainPresenter.SearchListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private MainPresenter mainPresenter;
    private int[] tabsIcon = {
            R.drawable.category_logo_2,
            R.drawable.location_logo_2,
            R.drawable.detail_logo_1,
            R.drawable.favorite_logo_1
    };

    @InjectView(R.id.dashboard_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.dashboard_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.dashboard_tab)
    TabLayout tabLayout;
    @InjectView(R.id.adView) AdView adView;
    private LocationFragment locationFragment;
    int locationId;
    public enum State {
        CATEGORY_PAGE(0), LOCATION_PAGE(1), DETAIL_PAGE(2), FAVORITE_PAGE(3);
        private int position;

        State(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mainPresenter = new MainPresenter();
        mainPresenter.create(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        updateUI();
        initAd();
        locationId = getIntent().getIntExtra(LOCATION_ID,0);
        if(locationId != 0){
            switchTabTo(DETAIL_PAGE.getPosition());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                switchTabTo(LOCATION_PAGE.getPosition());
                break;
            case R.id.menu_map:
                Intent intent = new MapIntent(this,true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fab_fade_in, R.anim.fab_fade_out);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == CATEGORY_PAGE.getPosition()) {
                return new CategoryFragment();
            } else if (position == LOCATION_PAGE.getPosition()) {
                locationFragment = LocationFragment.newInstance();
                return locationFragment;
            } else if (position == DETAIL_PAGE.getPosition()) {
                Bundle bundl = new Bundle();
                DetailFragment detailFragment = DetailFragment.newInstance();
                if(locationId!=0){
                    bundl.putInt("location_id",locationId);
                    detailFragment.setArguments(bundl);
                }
                return detailFragment;
            } else if(position == FAVORITE_PAGE.getPosition()){
                return FavoriteFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabsIcon.length;
        }
    }

    public void updateUI() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabsIcon[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    locationFragment.searchDefault();
                }else{
                    mainPresenter.querySearch(getRealm(), newText);
                    switchTabTo(LOCATION_PAGE.getPosition());
                }
                return false;
            }
        });
        return true;
    }

    public void switchTabTo(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void updateFromSearch(List<Locations> listLocation) {
        locationFragment.searchEvent(listLocation);
    }

    @Override
    public void switchToLocationDetail(Locations location) {
        switchTabTo(DETAIL_PAGE.getPosition());
    }

    public void initAd(){
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        AdRequest adRequest = adBuilder.build();
        adView.loadAd(adRequest);
    }
}
