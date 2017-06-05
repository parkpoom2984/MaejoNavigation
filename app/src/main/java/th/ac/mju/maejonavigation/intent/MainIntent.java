package th.ac.mju.maejonavigation.intent;

import android.content.Context;
import android.content.Intent;
import th.ac.mju.maejonavigation.screen.main.MainActivity;

/**
 * Created by Teh on 5/16/2017.
 */

public class MainIntent extends Intent {
    public static final String LOCATION_ID = "location_id";

    public MainIntent(Context context,int locationId) {
        super(context, MainActivity.class);
        putExtra(LOCATION_ID, locationId);
    }
}
