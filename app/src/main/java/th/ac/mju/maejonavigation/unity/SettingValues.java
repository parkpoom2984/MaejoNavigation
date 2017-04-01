package th.ac.mju.maejonavigation.unity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

import th.ac.mju.maejonavigation.model.Location;

/**
 * Created by Teh on 2/14/2017.
 */

public class SettingValues {

    public static final String CATEGORY_ICON = "category_icon_";
    public static final String CATEGORY_MARKER = "category_marker_";
    public static final String SELF_MARKER = "self_logo_1";
    public static final String IMAGE_LOCATION_PATH = "http://itsci.mju.ac.th/MaejoNavigation/photoLocations/";

    //public static String LINK_URL_JSONSTATUS = "http://itsci.mju.ac.th/MjnActivity/JsonStatusServlet";

    public static final String DEFAULT_LOCATION_NAME = "มหาวิทยาลัยแม่โจ้";
    public static final String DEFAULT_LOCATION_DETAIL = "มหาวิทยาลัยสีเขียว";
    public static final double DEFAULT_LOCATION_LATITUDE = 18.894993;
    public static final double DEFAULT_LOCATION_LONGITUDE = 99.009525;

    public static Typeface GET_FONT(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/THSarabunNew Bold.ttf");
    }

    //public static Location GET_DEFAULT_LOCATION(){
    //    //location.setLocationID(0);
    //    //location.setLocationName("มหาวิทยาลัยแม่โจ้");
    //    //location.setLocationDetails("มหาวิทยาลัยสีเขียว");
    //    //location.setImageLocationPath("https://upload.wikimedia.org/wikipedia/commons/4/40/Sydney_Opera_House_Sails.jpg");
    //    //location.setLatitude(0.0);
    //    //location.setLongitude(0.0);
    //    //location.setFavoriteStatus(-1);
    //    return new Location(0,);
    //}
    //public static String URLPATHMARKER = "http://maejonavit-thekitchen.rhcloud.com/markers/";
    //public static String MYLOCATION = "http://maejonavit-thekitchen.rhcloud.com/markers/user.png";
    //public static String IMAGEFlOOR_PATH = "http://itsci.mju.ac.th/MjnActivity/imageFloor/";
}
