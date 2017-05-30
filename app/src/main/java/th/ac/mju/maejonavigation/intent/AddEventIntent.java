package th.ac.mju.maejonavigation.intent;

import android.content.Context;
import android.content.Intent;

import th.ac.mju.maejonavigation.screen.main.MainActivity;

/**
 * Created by Sprites on 30/5/2560.
 */

public class AddEventIntent extends Intent {
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public AddEventIntent(Context context, double lat,double lng) {
        super(context, MainActivity.class);
        putExtra(LAT, lat);
        putExtra(LNG, lng);
    }
}
