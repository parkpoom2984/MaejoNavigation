package th.ac.mju.maejonavigation.prefer;

import android.content.SharedPreferences;

/**
 * Created by Teh on 5/19/2017.
 */

public class StringPreference {
    private final SharedPreferences sharedPreferences;
    private final String key;

    public StringPreference(SharedPreferences preferences,String key) {
        sharedPreferences = preferences;
        this.key = key;
    }

    public final boolean isSet() {
        return sharedPreferences.contains(key);
    }

    public final String get() {
        return sharedPreferences.getString(key,"");
    }

    public final void set(String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    public final void delete() {
        sharedPreferences.edit().remove(key).apply();
    }
}
