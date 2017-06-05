package th.ac.mju.maejonavigation.screen.main.event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.ListEvent;
import th.ac.mju.maejonavigation.request.MjnApi;

/**
 * Created by Teh on 5/24/2017.
 */

public class EventPresenter {

    private View listener;
    private MjnApi service;

    public EventPresenter(View listener, MjnApi service) {
        this.listener = listener;
        this.service = service;
    }

    public void getEvent() {
        Call<ListEvent> callListEvent = service.getListEvent();
        callListEvent.enqueue(new Callback<ListEvent>() {
            @Override
            public void onResponse(Call<ListEvent> call, Response<ListEvent> response) {
                List<Event> listEvent = response.body().getListEvent();
                listener.updateUI(listEvent);
            }

            @Override
            public void onFailure(Call<ListEvent> call, Throwable t) {

            }
        });
    }

    public interface View {
        void updateUI(List<Event> listEvent);
    }
}
