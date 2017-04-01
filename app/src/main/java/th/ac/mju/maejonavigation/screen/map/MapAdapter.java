package th.ac.mju.maejonavigation.screen.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.nitrico.mapviewpager.MapViewPager;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import th.ac.mju.maejonavigation.model.Location;

/**
 * Created by Teh on 3/28/2017.
 */

public class MapAdapter extends MapViewPager.MultiAdapter{
    //public static final String[] PAGE_TITLES = { "London", "Paris", "Brussels" };

    //public static final String[] ENGLAND_TITLES = { "London" };
    //public static final String[] FRANCE_TITLES = { "Paris" };
    //public static final String[] SPAIN_TITLES = { "Barcelona", "Madrid", "Valencia" };
    //public static final String[] PORTUGAL_TITLES = { };
    //public static final String[] ITALY_TITLES = { "Milan", "Rome" };
    //public static final String[] BELGIUM_TITLES = { "Brussels" };

    //public static final CameraPosition LONDON
     //       = CameraPosition.fromLatLngZoom(new LatLng(51.5287352,-0.381784), 6f);
    //public static final CameraPosition PARIS
     //       = CameraPosition.fromLatLngZoom(new LatLng(48.859,2.2074722), 6f);
    //public static final CameraPosition BARCELONA
    //        = CameraPosition.fromLatLngZoom(new LatLng(41.3948976,2.0787274), 6f);
    //public static final CameraPosition MADRID
    //        = CameraPosition.fromLatLngZoom(new LatLng(40.4381311,-3.8196227), 6f);
    //public static final CameraPosition VALENCIA
    //        = CameraPosition.fromLatLngZoom(new LatLng(39.4079665,-0.5015975), 6f);
    //public static final CameraPosition MILAN
    //        = CameraPosition.fromLatLngZoom(new LatLng(45.4628329,9.107692), 6f);
    //public static final CameraPosition ROME
    //        = CameraPosition.fromLatLngZoom(new LatLng(41.9102415,12.3959121), 6f);
    //public static final CameraPosition BRUSSELS
     //       = CameraPosition.fromLatLngZoom(new LatLng(50.8550625,4.3053499), 6f);

    //private List<CameraPosition> england;
    //private List<CameraPosition> france;
    //private List<CameraPosition> spain;
    //private List<CameraPosition> portugal;
    //private List<CameraPosition> italy;
    //private List<CameraPosition> belgium;
    private List<List<CameraPosition>> listTest;
    private List<Location> listLocation;
    public MapAdapter(FragmentManager fm,List<Location> listLocation) {
        super(fm);
        this.listLocation = listLocation;
        listTest = new ArrayList<>();
        for(Location location : listLocation){
            List<CameraPosition> test = new ArrayList<>();
            test.add(CameraPosition.fromLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 18f));
            listTest.add(test);
        }
        // camera positions
        //england = new ArrayList<>();
        //france = new ArrayList<>();
        //spain = new ArrayList<>();
        //portugal = new ArrayList<>();
        //italy = new ArrayList<>();
        //belgium = new ArrayList<>();

        //england.add(LONDON);
        //france.add(PARIS);
        //spain.add(BARCELONA);
        //spain.add(MADRID);
        //spain.add(VALENCIA);
        //italy.add(MILAN);
        //italy.add(ROME);
        //belgium.add(BRUSSELS);
    }

    @Override
    public int getCount() {
        return listLocation.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardViewPagerMap.newInstance(listLocation.get(position));
        //return  new CardViewPagerMap();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listLocation.get(position).getLocationName();
    }

    @Override
    public String getMarkerTitle(int page, int position) {
        //switch (page) {
        //    case 0: return ENGLAND_TITLES[position];
        //    case 1: return FRANCE_TITLES[position];
        //    case 2: return BELGIUM_TITLES[position];
        //    //case 2: return SPAIN_TITLES[position];
        //    //case 3: return PORTUGAL_TITLES[position];
        //    //case 4: return ITALY_TITLES[position];
        //    //case 5: return BELGIUM_TITLES[position];
        //}

        for(int i=0;i<listLocation.size();i++){
            if(i == page){
                return listLocation.get(page).getLocationName();
            }
        }
        return null;
    }

    @Override
    public List<CameraPosition> getCameraPositions(int page) {
        //switch (page) {
        //    case 0: return england;
        //    case 1: return france;
        //    case 2: return belgium;
        //    //case 3: return portugal;
        //    //case 4: return italy;
        //    //case 5: return belgium;
        //}
        for(int i=0;i<listTest.size();i++){
            if(i == page){
                return listTest.get(page);
            }
        }
        return null;
    }
}
