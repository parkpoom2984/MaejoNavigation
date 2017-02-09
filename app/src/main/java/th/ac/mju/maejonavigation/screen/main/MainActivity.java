package th.ac.mju.maejonavigation.screen.main;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.screen.main.category.CategoryFragment;
import th.ac.mju.maejonavigation.screen.main.detail.DetailFragment;
import th.ac.mju.maejonavigation.screen.main.favorite.FavoriteFragment;
import th.ac.mju.maejonavigation.screen.main.location.LocationFragment;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private int[] tabsIcon ={
            R.drawable.category_logo_2,
            R.drawable.location_logo_2,
            R.drawable.detail_logo_1,
            R.drawable.favorite_logo_1
    };

    @InjectView(R.id.dashboard_toolbar) Toolbar toolbar;
    @InjectView(R.id.dashboard_viewpager) ViewPager mViewPager;
    @InjectView(R.id.dashboard_tab) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 :
                    return new CategoryFragment();
                case 1 :
                    return new LocationFragment();
                case 2 :
                    return new DetailFragment();
                case 3 :
                    return new FavoriteFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void updateUI(){

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(0);

        tabLayout.setupWithViewPager(mViewPager);
        for(int i = 0 ; i < mSectionsPagerAdapter.getCount() ;i++ ) {
            tabLayout.getTabAt(i).setIcon(tabsIcon[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //do here when have event text change
                return false;
            }
        });
        return true;
    }

}
