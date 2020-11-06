package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by dell on 15-08-2018.
 */

public class CityPrefer {
    SharedPreferences preferences;

    public CityPrefer(Activity activity){
        preferences=activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return preferences.getString("city","Mumbai");

    }
    public void setCity(String city){
        preferences.edit().putString("city",city).commit();
    }
}
