package th.ac.mju.maejonavigation.screen.main.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.eventbus.Subscribe;
import com.viewpagerindicator.LinePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.unity.SettingValues;


public class DetailFragment extends MjnFragment {

    @InjectView(R.id.location_view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.map_view)
    MapView mMapView;
    //@InjectView(R.id.swipe_refreshLayout)
    //SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.detail_location_name)
    TextView locationName;
    @InjectView(R.id.detail_location)
    TextView locationDetail;
    @InjectView(R.id.indicator)
    LinePageIndicator indicator;
    @InjectView(R.id.view_pager_layout)
    RelativeLayout viewPagerLayout;


    @InjectView(R.id.card_default)
    CardView defaultCard;
    @InjectView(R.id.card_detail_location)
    CardView locationCard;
    @InjectView(R.id.card_detail_map)
    CardView mapCard;
    @InjectView(R.id.detail_image_default)
    ImageView imageDefault;
    @InjectView(R.id.detail_name_default)TextView nameDefault;
    EventBus bus = EventBus.getDefault();

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    private enum State{
        DEFAULT_PAGE,DETAIL_PAGE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, view);
        mMapView.onCreate(savedInstanceState);
        setState(State.DEFAULT_PAGE);
        updateDefaultPage();
        //swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
        //
        //    @Override
        //    public void onRefresh() {
        //        refreshItems();
        //    }
        //});
        return view;
    }

    private void updateDefaultPage() {
        imageDefault.setImageResource(R.drawable.maejo_default);
        nameDefault.setText(SettingValues.DEFAULT_LOCATION_NAME);
    }


    public class ImagePager extends PagerAdapter {

        @InjectView(R.id.item_pager_image)
        ImageView image_detail;
        Context mContext;
        LayoutInflater mLayoutInflater;
        private String imageUrl;

        public ImagePager(Context context, String imageUrl) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            this.imageUrl = imageUrl;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (LinearLayout) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_image_detail, container, false);
            ButterKnife.inject(this, itemView);
            Context context = itemView.getContext();
            Glide.with(context)
                    .load(SettingValues.IMAGE_LOCATION_PATH + imageUrl + ".jpg")
                    .placeholder(R.drawable.img_default)
                    .into(image_detail);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    //public void refreshItems(){
    //    ImagePager mWelcomePagerAdapter = new ImagePager(getActivity());
    //    mViewPager.setAdapter(mWelcomePagerAdapter);
    //    onRefreshCompleted();
    //}

    //private void onRefreshCompleted() {
    //    swipeRefreshLayout.setRefreshing(false);
    //}

    @Subscribe
    public void onEvent(SelectLocationEvent event) {
        setState(State.DETAIL_PAGE);
        final Location location = event.getLocation();
        locationName.setText(location.getLocationName());
        locationDetail.setText(location.getLocationDetails());
        ImagePager mWelcomePagerAdapter = new ImagePager(getActivity(),
                location.getImageLocationPath());
        mViewPager.setAdapter(mWelcomePagerAdapter);
        indicator.setViewPager(mViewPager);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        int icon = getResources().getIdentifier(
                SettingValues.CATEGORY_MARKER + location.getCategoryId(), "drawable",
                getContext().getPackageName());
        updateMapUI(latLng, icon);
        //mMapView.getMapAsync(new OnMapReadyCallback() {
        //    @Override
        //    public void onMapReady(GoogleMap googleMap) {
        //        googleMap.addMarker(new MarkerOptions().position(
        //                new LatLng(location.getLatitude(), location.getLongitude())).icon(
        //                BitmapDescriptorFactory.defaultMarker()));
        //
        //        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng
        // (location.getLatitude(), location.getLongitude()), 10);
        //        googleMap.animateCamera(cameraUpdate);
        //    }
        //});
        //BitmapDescriptorFactory.fromResource(R.drawable.category_marker_1)
    }

    @OnClick(R.id.detail_get_direction)
    public void onClickGetDirection() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
        startActivity(intent);
    }

    public void updateMapUI(final LatLng latLng, final int icon) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(icon))
                        .position(latLng));
                googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom((latLng), 15)));
                //googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                //    @Override
                //    public boolean onMarkerClick(Marker marker) {
                //        marker.showInfoWindow();
                //        return false;
                //    }
                //});
                //googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                //    @Override
                //    public void onMapClick(LatLng latLng) {
                //
                //    }
                //});
            }
        });
    }


    public void setState(State state){
        switch (state){
            case DEFAULT_PAGE:
                defaultCard.setVisibility(View.VISIBLE);
                locationCard.setVisibility(View.GONE);
                mapCard.setVisibility(View.GONE);
                break;
            case DETAIL_PAGE:
                defaultCard.setVisibility(View.GONE);
                locationCard.setVisibility(View.VISIBLE);
                mapCard.setVisibility(View.VISIBLE);
                break;
        }
    }
}
