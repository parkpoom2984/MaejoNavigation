package th.ac.mju.maejonavigation.screen.main.event;


import android.os.Bundle;
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
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.dialog.EventDialog;
import th.ac.mju.maejonavigation.fragment.MjnFragment;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.screen.main.MainActivity;


public class EventFragment extends MjnFragment
        implements EventAdapter.CardOnClick, EventPresenter.View {

    @InjectView(R.id.event_recycler_view)
    RecyclerView eventRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.inject(this, view);
        eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        eventRecyclerView.setLayoutManager(layoutManager);
        EventPresenter eventPresenter = new EventPresenter(this, getService());
        eventPresenter.getEvent();
        return view;
    }


    @OnClick(R.id.event_add)
    public void onClickAdd() {
        ((MainActivity) getActivity()).goTo();
    }

    @Override
    public void onClickEvent(Event event) {
        EventDialog dialog = new EventDialog(getContext(), event, getRealm());
        dialog.show();
    }

    @Override
    public void updateUI(List<Event> listEvent) {
        List<Event> value = new ArrayList<>();
        for (Event event : listEvent) {
            if (event.getStatus() == 1) {
                value.add(event);
            }
        }
        eventRecyclerView.setAdapter(new EventAdapter(listEvent, getRealm(), this));
    }
}
