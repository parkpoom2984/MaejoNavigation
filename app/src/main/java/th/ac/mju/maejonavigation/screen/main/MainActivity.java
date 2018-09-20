package th.ac.mju.maejonavigation.screen.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import butterknife.OnClick;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;

import th.ac.mju.maejonavigation.dialog.AboutUsDialogFragment;
import th.ac.mju.maejonavigation.dialog.Dialogs;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.addevent.AddEventActivity;
import th.ac.mju.maejonavigation.screen.main.category.CategoryFragment;
import th.ac.mju.maejonavigation.screen.main.detail.DetailFragment;

import th.ac.mju.maejonavigation.screen.main.event.EventFragment;
import th.ac.mju.maejonavigation.screen.main.favorite.FavoriteFragment;
import th.ac.mju.maejonavigation.screen.main.location.LocationFragment;

import static th.ac.mju.maejonavigation.intent.MainIntent.LOCATION_ID;
import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.*;

public class MainActivity extends MjnActivity implements MainPresenter.SearchListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String LOCATION_ID_BUNDLE = "location_id";
    private MainPresenter mainPresenter;
    private int[] tabsIcon = {
            R.drawable.category_logo_2,
            R.drawable.location_logo_2,
            R.drawable.detail_logo_1,
            R.drawable.favorite_logo_1,
            R.drawable.ic_event_white
    };

    @InjectView(R.id.dashboard_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.dashboard_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.dashboard_tab)
    TabLayout tabLayout;
    @InjectView(R.id.adView)
    AdView adView;
    private LocationFragment locationFragment;
    private int locationId;
    public int REQUEST_CODE = 12;

    public enum State {
        CATEGORY_PAGE(0), LOCATION_PAGE(1), DETAIL_PAGE(2), FAVORITE_PAGE(3), EVENT_PAGE(4);
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
        locationId = getIntent().getIntExtra(LOCATION_ID, 0);
        if (locationId != 0) {
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
                Intent intent = new MapIntent(this, true);
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
                Bundle bundle = new Bundle();
                DetailFragment detailFragment = DetailFragment.newInstance();
                if (locationId != 0) {
                    bundle.putInt(LOCATION_ID_BUNDLE, locationId);
                    detailFragment.setArguments(bundle);
                }
                return detailFragment;
            } else if (position == FAVORITE_PAGE.getPosition()) {
                return FavoriteFragment.newInstance();
            } else if (position == EVENT_PAGE.getPosition()) {
                return new EventFragment();
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
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    locationFragment.searchDefault();
                } else {
                    mainPresenter.querySearch(getRealm(), newText);
                    switchTabTo(LOCATION_PAGE.getPosition());
                }
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                locationFragment.searchDefault();
                return true;
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

    public void initAd() {
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        AdRequest adRequest = adBuilder.build();
        adView.loadAd(adRequest);
    }

    @OnClick(R.id.main_logo)
    public void onClickLogo() {
        Dialogs.show(MainActivity.this, new AboutUsDialogFragment());
    }


    public void goTo() {
        startActivityForResult(new Intent(MainActivity.this, AddEventActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(adView, R.string.msg_add_event, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void checkNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Snackbar snackbar = Snackbar.make(adView, R.string.internet_can_not_connect,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.try_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAvailable();
            }
        });
        if (!isConnect) {
            snackbar.show();
        } else {
            snackbar.dismiss();
        }
    }
}
