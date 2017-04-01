package th.ac.mju.maejonavigation.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by Teh on 3/24/2017.
 */

public class BooleanPreference{

    private final SharedPreferences sharedPreferences;
    private final String key;

    public BooleanPreference(SharedPreferences preferences,String key) {
        sharedPreferences = preferences;
        this.key = key;
    }

    public final boolean isSet() {
        return sharedPreferences.contains(key);
    }

    public final boolean get() {
        return sharedPreferences.getBoolean(key,false);
    }

    public final void set(boolean value) {
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    public final void delete() {
        sharedPreferences.edit().remove(key).apply();
    }
}
