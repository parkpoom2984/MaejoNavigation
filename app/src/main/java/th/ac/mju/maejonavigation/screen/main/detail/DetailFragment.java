package th.ac.mju.maejonavigation.screen.main.detail;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.dialog.ChooseFloorDialog;
import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.intent.MapIntent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.MainActivity;
import th.ac.mju.maejonavigation.unity.SettingValues;


public class DetailFragment extends MjnFragment {

    @InjectView(R.id.map_view)
    MapView mMapView;
    @InjectView(R.id.detail_location_name)
    TextView locationName;
    @InjectView(R.id.detail_location)
    TextView locationDetail;
    @InjectView(R.id.view_pager_layout)
    RelativeLayout viewPagerLayout;
    @InjectView(R.id.location_image_detail)
    ImageView imageLocation;
    @InjectView(R.id.card_default)
    CardView defaultCard;
    @InjectView(R.id.card_detail_location)
    CardView locationCard;
    @InjectView(R.id.card_detail_map)
    CardView mapCard;
    @InjectView(R.id.detail_image_default)
    ImageView imageDefault;
    @InjectView(R.id.detail_name_default)
    TextView nameDefault;
    Locations location;
    @InjectView(R.id.plan_line)
    View line;
    @InjectView(R.id.plan_box_view)
    FrameLayout boxViewPlan;
    @InjectView(R.id.detail_icon_favorite)
    ImageView iconFavorite;

    private enum State {
        DEFAULT_PAGE, DETAIL_PAGE;
    }

    private static final String LOCATION_ID_BUNDLE = "location_id";
    private static final String LOCATION_ID_FIELD = "locationId";

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, view);
        mMapView.onCreate(savedInstanceState);
        setState(State.DEFAULT_PAGE);
        updateDefaultPage();
        if (getArguments() != null) {
            final int locationId = getArguments().getInt(LOCATION_ID_BUNDLE);
            getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    location = realm.where(Locations.class)
                            .equalTo(LOCATION_ID_FIELD, locationId)
                            .findFirst();
                    updateLocationDetail(location);
                }
            });
        }
        return view;
    }

    private void updateDefaultPage() {
        imageDefault.setImageResource(R.drawable.maejo_default);
        nameDefault.setText(SettingValues.DEFAULT_LOCATION_NAME);
    }

    @Subscribe
    public void onEvent(SelectLocationEvent event) {
        location = event.getLocation();
        updateLocationDetail(location);
    }

    public void updateLocationDetail(Locations location) {
        setState(State.DETAIL_PAGE);
        locationName.setText(location.getLocationName());
        locationDetail.setText(location.getLocationDetails());
        boolean isFloorEmpty = location.getListFloor().size() == 0;
        boxViewPlan.setVisibility(isFloorEmpty ? View.GONE : View.VISIBLE);
        line.setVisibility(isFloorEmpty ? View.GONE : View.VISIBLE);
        Glide.with(this)
                .load(SettingValues.IMAGE_LOCATION_PATH + location.getImageLocationPath() + ".jpg")
                .placeholder(R.drawable.img_default)
                .priority(Priority.LOW)
                .into(imageLocation);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        int icon = getResources().getIdentifier(
                SettingValues.CATEGORY_MARKER + location.getCategoryId(), "drawable",
                getContext().getPackageName());
        showIconFavorite(location.getFavoriteStatus());
        updateMapUI(latLng, icon);
        ((MainActivity) getActivity()).checkNetworkAvailable();
    }

    @OnClick(R.id.detail_get_direction)
    public void onClickGetDirection() {
        Intent intent = new MapIntent(getContext(), false, location);
        startActivity(intent);
    }

    @OnClick(R.id.plan_box_view)
    public void onClickViewPlan() {
        ChooseFloorDialog dialog = new ChooseFloorDialog(getContext(), location.getLocationName(),
                location.getListFloor());
        dialog.show();
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
            }
        });
        mMapView.setClickable(false);
    }


    public void setState(State state) {
        switch (state) {
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

    @OnClick(R.id.detail_icon_favorite)
    public void onClickIconFavorite() {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Locations locationEdit = realm.where(Locations.class).equalTo(LOCATION_ID_FIELD,
                        location.getLocationId()).findFirst();
                locationEdit.setFavoriteStatus(locationEdit.getFavoriteStatus() * -1);
                realm.copyToRealmOrUpdate(locationEdit);
                showIconFavorite(locationEdit.getFavoriteStatus());
            }
        });
        ((MainActivity) getActivity()).switchTabTo(1);
        ((MainActivity) getActivity()).switchTabTo(2);
    }


    public void showIconFavorite(int favoriteStatus) {
        if (favoriteStatus == 1) {
            iconFavorite.setImageResource(R.drawable.fav_full);
        } else {
            iconFavorite.setImageResource(R.drawable.fav_blank);
        }
    }
}
