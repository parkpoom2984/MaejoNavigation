package th.ac.mju.maejonavigation.intent;

import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import th.ac.mju.maejonavigation.model.Floor;
import th.ac.mju.maejonavigation.screen.plan.PlanActivity;

/**
 * Created by Teh on 5/11/2017.
 */

public class PlanIntent extends Intent{
    public static final String LOCATION_NAME = "location_name";
    public static final String FLOOR = "floor";
    public static final String ROOM_ID = "room_id";

    public PlanIntent(Context context,String locationName,Floor floor) {
        super(context, PlanActivity.class);
        putExtra(LOCATION_NAME, locationName);
        putExtra(FLOOR, Parcels.wrap(floor));
    }

    public PlanIntent(Context context,String locationId,Floor floor,int roomId) {
        super(context, PlanActivity.class);
        putExtra(LOCATION_NAME, locationId);
        putExtra(FLOOR, Parcels.wrap(floor));
        putExtra(ROOM_ID, roomId);
    }
}
