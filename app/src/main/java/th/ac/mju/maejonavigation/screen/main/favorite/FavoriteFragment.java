package th.ac.mju.maejonavigation.screen.main.favorite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.event.SelectLocationEvent;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.main.MainActivity;
import th.ac.mju.maejonavigation.screen.main.location.LocationAdapter;
import th.ac.mju.maejonavigation.screen.main.location.LocationFragment;

import static th.ac.mju.maejonavigation.screen.main.MainActivity.State.DETAIL_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends MjnFragment
        implements FavoritePresenter.View, LocationAdapter.LocationClick {


    @InjectView(R.id.favorite_recycler_view)
    RecyclerView recyclerView;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.inject(this, view);
        FavoritePresenter favoritePresenter = new FavoritePresenter();
        favoritePresenter.create(this, getRealm());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(inflater.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        favoritePresenter.queryLocationFavorite();
        return view;
    }

    @Override
    public void showListLocationFavorite(List<Locations> listLocation) {
        LocationAdapter locationAdapter = new LocationAdapter(listLocation, this,
                LocationFragment.State.SELECT);
        recyclerView.setAdapter(locationAdapter);
    }

    @Override
    public void onClick(Locations location) {
        getBus().post(new SelectLocationEvent(location));
        ((MainActivity) getActivity()).switchTabTo(DETAIL_PAGE.getPosition());
    }
}
