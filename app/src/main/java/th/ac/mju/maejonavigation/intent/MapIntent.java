package th.ac.mju.maejonavigation.intent;

import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import th.ac.mju.maejonavigation.model.Locations;
import th.ac.mju.maejonavigation.screen.map.MapActivity;

/**
 * Created by Teh on 5/8/2017.
 */

public class MapIntent extends Intent {
    public static final String TYPE_ALL_LOCATION = "type_all_location";
    public static final String LOCATION_DIRECTION = "location_direction";

    public MapIntent(Context context,Boolean typeAllLocation) {
        super(context, MapActivity.class);
        putExtra(TYPE_ALL_LOCATION, typeAllLocation);
    }

    public MapIntent(Context context,Boolean typeAllLocation,Locations locations){
        super(context, MapActivity.class);
        putExtra(TYPE_ALL_LOCATION, typeAllLocation);
        putExtra(LOCATION_DIRECTION, Parcels.wrap(locations));
    }
}
