package th.ac.mju.maejonavigation.screen.main.location;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.model.Room;
import th.ac.mju.maejonavigation.unity.SettingValues;

/**
 * Created by Teh on 2/18/2017.
 */

public class LocationAdapter extends  RecyclerView.Adapter<LocationAdapter.ViewHolder> implements
        View.OnClickListener{

    private List<Locations> listLocation;
    private LocationClick listener;
    private LocationFragment.State state;
    public LocationAdapter(List<Locations> listLocation,LocationClick listener,LocationFragment.State state){
        this.listLocation = listLocation;
        this.listener = listener;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_location,null);
        ViewHolder viewHolder = new ViewHolder(card_view);
        card_view.setOnClickListener(this);
        viewHolder.cardView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Locations location = listLocation.get(position);
        holder.cardView.setTag(location);
        Context context = holder.iconLocation.getContext();
        String categoryIconName = SettingValues.CATEGORY_ICON+location.getCategoryId();
        holder.iconLocation.setImageResource(context.getResources().getIdentifier(
                categoryIconName, "drawable", context.getPackageName()));
        holder.nameLocation.setText(location.getLocationName());
        String text ="";
        if(location.getListFloor().size()>0){
            for (Floor floor : location.getListFloor()){
                for (Room room : floor.getListRoom()){
                    text += room.getRoomName()+",";
                }
            }
        }
        boolean isSearch = state == LocationFragment.State.SEARCH;
        holder.roomName.setText(text);
        holder.roomName.setVisibility(isSearch && !text.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return listLocation.size();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()  == R.id.card_view){
            listener.onClick((Locations) view.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.location_icon_imageview) ImageView iconLocation;
        @InjectView(R.id.location_name_textview) TextView  nameLocation;
        @InjectView(R.id.location_room_textview) TextView roomName;
        @InjectView(R.id.card_view)
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface LocationClick{
        void onClick(Locations location);
    }
}
