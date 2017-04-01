package th.ac.mju.maejonavigation.screen.main.location;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.common.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.event.SearchEvent;
import th.ac.mju.maejonavigation.event.SelectCategoryEvent;
import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.DETAIL_PAGE;

public class LocationFragment extends MjnFragment implements LocationAdapter.LocationClick,LocationPresenter.View{
    @InjectView(R.id.location_recycler_view) RecyclerView mRecyclerView;
    @InjectView(R.id.location_not_found_search)
    LinearLayout notFoundLayout;
    private LocationPresenter locationPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        ButterKnife.inject(this,view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(inflater.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        EventBus bus = EventBus.getDefault();
        bus.register(this);
        locationPresenter = new LocationPresenter();
        locationPresenter.create(this,getRealm());
        locationPresenter.queryListLocation();

        return view;
    }



    @Subscribe
    public void onEvent(SelectCategoryEvent event) {
        int categoryId = event.getCategory().getCategoryId();
        locationPresenter.queryListLocationByCategory(categoryId);
    }

    @Subscribe
    public void onEvent(SearchEvent event){
        List<Location> listLocation = event.getListLocation();
        if(listLocation.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
            notFoundLayout.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            notFoundLayout.setVisibility(View.GONE);
            LocationAdapter locationAdapter = new LocationAdapter(event.getListLocation(),this);
            mRecyclerView.setAdapter(locationAdapter);
        }
    }

    @Override
    public void onClick(Location location) {
        EventBus bus = EventBus.getDefault();
        bus.post(new SelectLocationEvent(location));
        ((MainActivity) getActivity()).switchTabTo(DETAIL_PAGE.getPosition());
    }

    @Override
    public void showListLocation(List<Location> listLocation) {
        LocationAdapter locationAdapter = new LocationAdapter(listLocation,this);
        mRecyclerView.setAdapter(locationAdapter);
    }

    @Override
    public void showListLocationByCategory(List<Location> listLocationByCategory) {
        LocationAdapter locationAdapter = new LocationAdapter(listLocationByCategory,this);
        mRecyclerView.setAdapter(locationAdapter);
    }
}
