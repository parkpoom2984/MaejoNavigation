package th.ac.mju.maejonavigation.unity;

import android.content.Context;
import android.graphics.Typeface;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Teh on 2/14/2017.
 */

public class SettingValues {

    public static final String CATEGORY_ICON = "category_icon_";
    public static final String CATEGORY_MARKER = "category_marker_";
    public static final String SELF_MARKER = "self_logo_1";
    public static final String IMAGE_LOCATION_PATH = "http://itsci.mju.ac.th/MaejoNavigation/photoLocations/";
    public static String IMAGEFlOOR_PATH = "http://itsci.mju.ac.th/MaejoNavigation/imageFloor/";

    public static final String DEFAULT_LOCATION_NAME = "มหาวิทยาลัยแม่โจ้";

    public static Typeface GET_FONT(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/THSarabunNew Bold.ttf");
    }

    public static LatLng getLatLngMaejo(){
        return new LatLng(18.896365, 99.013325);
    }
}
