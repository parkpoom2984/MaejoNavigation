package th.ac.mju.maejonavigation.screen.main.event;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.dialog.EventDialog;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.ListEvent;
import th.ac.mju.maejonavigation.screen.main.MainActivity;


public class EventFragment extends MjnFragment implements EventAdapter.CardOnClick {

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
                List<Event> listEvent = response.body().getListEvent();
                updateEventRecyclerView(listEvent);
            }

            @Override
            public void onFailure(Call<ListEvent> call, Throwable t) {

            }
        });

    }

    private void updateEventRecyclerView(List<Event> listEvent) {
        List<Event> value = new ArrayList<>();
        for(Event event : listEvent){
            if(event.getStatus() == 1){
                value.add(event);
            }
        }
        setEventRecyclerView(value);
    }

    public void setEventRecyclerView(List<Event> listEvent){
        eventRecyclerView.setAdapter(new EventAdapter(listEvent,getRealm(),this));
    }


    @OnClick(R.id.event_add)
    public void onClickAdd(){
        ((MainActivity)getActivity()).goTo();
    }

    @Override
    public void onClickEvent(Event event) {
        EventDialog dialog = new EventDialog(getContext(),event,getRealm());
        dialog.show();
    }
}
