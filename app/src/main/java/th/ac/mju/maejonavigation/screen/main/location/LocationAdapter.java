package th.ac.mju.maejonavigation.screen.main.location;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.model.Location;
import th.ac.mju.maejonavigation.model.Room;
import th.ac.mju.maejonavigation.unity.SettingValues;

/**
 * Created by Teh on 2/18/2017.
 */

public class LocationAdapter extends  RecyclerView.Adapter<LocationAdapter.ViewHolder> implements
        View.OnClickListener{

    private List<Location> listLocation;
    private LocationClick listener;
    public LocationAdapter(List<Location> listLocation,LocationClick listener){
        this.listLocation = listLocation;
        this.listener = listener;
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
        Location location = listLocation.get(position);
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

        holder.roomName.setText(text);
        //int i = 0;
        //if(location.getListFloor().size()> 0) {
        //    for (Floor floor : location.getListFloor()) {
        //        for (Room room : floor.getListRoom()) {
        //            SwipeItem swipeItem = new SwipeItem(i,room.getRoomName(), floor.getFloorName());
        //            holder.swipeSelector.setItems(swipeItem);
        //            holder.swipeSelector.setItems(swipeItem);
        //            i++;
        //        }
        //    }
        //}
    }

    @Override
    public int getItemCount() {
        return listLocation.size();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()  == R.id.card_view){
            listener.onClick((Location) view.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.location_icon_imageview) ImageView iconLocation;
        @InjectView(R.id.location_name_textview) TextView  nameLocation;
        @InjectView(R.id.location_room_textview) TextView roomName;
        //@InjectView(R.id.location_swipe_selector)
        //SwipeSelector swipeSelector;
        @InjectView(R.id.card_view)
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    interface LocationClick{
        void onClick(Location location);
    }
}
