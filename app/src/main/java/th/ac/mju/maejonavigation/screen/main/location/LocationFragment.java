package th.ac.mju.maejonavigation.screen.main.location;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.event.SearchEvent;
import th.ac.mju.maejonavigation.event.SelectCategoryEvent;
import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.intent.PlanIntent;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.MainActivity;
import th.ac.mju.maejonavigation.screen.main.detail.DetailFragment;
import th.ac.mju.maejonavigation.screen.plan.PlanActivity;

import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.DETAIL_PAGE;

public class LocationFragment extends MjnFragment implements LocationAdapter.LocationClick,LocationPresenter.View{
    @InjectView(R.id.location_recycler_view) RecyclerView mRecyclerView;
    @InjectView(R.id.location_not_found_search)
    LinearLayout notFoundLayout;
    private LocationPresenter locationPresenter;
    public static LocationFragment newInstance(){
        return new LocationFragment();
    }
    public enum State{
        SEARCH,SELECT
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        ButterKnife.inject(this,view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(inflater.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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

    @Override
    public void onClick(Locations location) {
        int countResult=0;
        if(location.getListFloor().size()==1){
            if(location.getListFloor().get(0).getListRoom().size()==1){
                countResult++;
            }
        }
        if(countResult==1){
            Floor floor = location.getListFloor().get(0);
            int roomId = floor.getListRoom().get(0).getRoomId();
            Intent intent = new PlanIntent(getContext(),location.getLocationName(),floor,roomId);
            startActivity(intent);
        }else{
            getBus().post(new SelectLocationEvent(location));
            ((MainActivity) getActivity()).switchTabTo(DETAIL_PAGE.getPosition());
        }
    }

    @Override
    public void showListLocation(List<Locations> listLocation) {
        LocationAdapter locationAdapter = new LocationAdapter(listLocation,this,State.SELECT);
        mRecyclerView.setAdapter(locationAdapter);
    }

    @Override
    public void showListLocationByCategory(List<Locations> listLocationByCategory) {
        LocationAdapter locationAdapter = new LocationAdapter(listLocationByCategory,this,State.SELECT);
        mRecyclerView.setAdapter(locationAdapter);
    }

    public void searchEvent(List<Locations> listLocation){
        if(listLocation.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
            notFoundLayout.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            notFoundLayout.setVisibility(View.GONE);
            LocationAdapter locationAdapter = new LocationAdapter(listLocation,this,State.SEARCH);
            mRecyclerView.setAdapter(locationAdapter);
        }
    }

    public void searchDefault(){
        locationPresenter.queryListLocation();
    }
}
