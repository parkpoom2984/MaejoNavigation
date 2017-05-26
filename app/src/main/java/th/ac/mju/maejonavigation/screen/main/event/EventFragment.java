package th.ac.mju.maejonavigation.screen.main.event;


import android.content.Intent;
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
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.DataStatus;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.ListCategory;
import th.ac.mju.maejonavigation.model.ListEvent;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.addevent.AddEventActivity;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends MjnFragment {

    @InjectView(R.id.event_recycler_view)
    RecyclerView eventRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.inject(this,view);
        eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        eventRecyclerView.setLayoutManager(layoutManager);
        updateUI();
        return view;
    }

    public void updateUI(){
        Call<ListEvent> callListEvent = getService().getListEvent();
        callListEvent.enqueue(new Callback<ListEvent>() {
            @Override
            public void onResponse(Call<ListEvent> call, Response<ListEvent> response) {
                final List<Event> listEvent = response.body().getListEvent();
                eventRecyclerView.setAdapter(new EventAdapter(listEvent,getRealm()));
            }

            @Override
            public void onFailure(Call<ListEvent> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.event_add)
    public void onClickAdd(){
        ((MainActivity)getActivity()).goTo();
    }
}
