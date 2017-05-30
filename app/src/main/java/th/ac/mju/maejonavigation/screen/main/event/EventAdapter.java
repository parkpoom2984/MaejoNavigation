package th.ac.mju.maejonavigation.screen.main.event;

import android.location.Location;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.model.Category;
import th.ac.mju.maejonavigation.model.Event;
import th.ac.mju.maejonavigation.model.Locations;

/**
 * Created by Teh on 5/24/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements
        View.OnClickListener {
    private List<Event> listEvent;
    private Realm realm;
    private CardOnClick listener;
    public EventAdapter(List<Event> listEvent,Realm realm,CardOnClick listener) {
        this.listEvent = listEvent;
        this.realm = realm;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event,null);
        ViewHolder viewHolder = new ViewHolder(card_view);
        card_view.setOnClickListener(this);
        viewHolder.cardEvent.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Event event = listEvent.get(position);
        holder.cardEvent.setTag(event);
        holder.eventDate.setText(event.getEventDate());
        holder.eventTitle.setText(event.getEventName());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Locations location = realm.where(Locations.class).equalTo("locationId",event.getLocationId()).findFirst();
                holder.eventLocation.setText(location.getLocationName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.event_card_view) {
            listener.onClickEvent((Event) view.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.event_date) TextView eventDate;
        @InjectView(R.id.event_location) TextView eventLocation;
        @InjectView(R.id.event_title) TextView eventTitle;
        @InjectView(R.id.event_card_view) CardView cardEvent;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

    interface CardOnClick{
        void onClickEvent(Event event);
    }
}
