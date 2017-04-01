package th.ac.mju.maejonavigation.screen.map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.unity.SettingValues;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardViewPagerMap extends MjnFragment {

    @InjectView(R.id.map_location_image)
    ImageView locationImageView;
    @InjectView(R.id.map_location_detail)
    TextView locationDetailTextView;

    private String locationDetail;
    private String locationUrlImage;
    private static final String LOCATION_DETAIL = "LOCATION_DETAIL";
    private static final String LOCATION_URL_PATH = "LOCATION_URL_PATH";

    public CardViewPagerMap() {

    }

    public static CardViewPagerMap newInstance(Location location) {
        CardViewPagerMap f = new CardViewPagerMap();
        Bundle args = new Bundle();
        args.putString(LOCATION_DETAIL,location.getLocationDetails());
        args.putString(LOCATION_URL_PATH,location.getImageLocationPath());
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            locationDetail = args.getString(LOCATION_DETAIL);
            locationUrlImage = args.getString(LOCATION_URL_PATH);
        }

        ViewCompat.setElevation(getView(), 10f);
        locationDetailTextView.setText(locationDetail);
        Glide.with(getContext())
                .load(SettingValues.IMAGE_LOCATION_PATH+locationUrlImage+".jpg")
                .placeholder(R.drawable.img_default)
                .into(locationImageView);
        //ViewCompat.setElevation(toolbar, 4f);

        //toolbar.setTitle(MapAdapter[index]);
        //toolbar.inflateMenu(R.menu.menu_fragment);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        getActivity().onBackPressed();
        //    }
        //});
    }

}
