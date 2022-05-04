package Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import classes.TopTenItem;

public class UtilityMethods extends AppCompatActivity {

    public static final String FILE_NAME = "top_ten";
    public static final String KEY_PREFS = "top_ten_item";

    /**
     * switch activity
     * @param context - the context of the activity that calls the switch
     * @param dest - destination activity
     */
    public static void switchActivity(Context context, Class dest) {
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * function to get the best possible location, after testing all enables location providers
     * @param ctx - context of the activity that calls this function
     * @return Location object that represents saved location
     * @throws Exception in case there are no given location permissions for the app
     */
    public static Location getLocation(Context ctx) throws Exception {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<String> providers = lm.getProviders(true);
            Location bestLoc = null;

            for (String provider : providers) {
                Location candidate = lm.getLastKnownLocation(provider);

                if (candidate == null)
                    continue;

                if (bestLoc == null || candidate.getAccuracy() < bestLoc.getAccuracy())
                    bestLoc = candidate;
            }
            return bestLoc;
        }
        throw new Exception("Error: missing location permissions!");
    }

    /**
     * Save an arraylist of items to the shared preference file
     * @param ctx - context of the activity that calls this function
     * @param items - an Arraylist of items to be saved into the shared preference file
     */
    public static void saveTopTen(Context ctx, List<TopTenItem> items) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        setList(KEY_PREFS, items, editor);
    }

    /**
     * Load a list of items from the shared preference file into an Arraylist
     * @param ctx - context of the activity that calls this function
     * @return Arraylist of items loaded from the file
     */
    public static ArrayList<TopTenItem> loadTopTen(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, ctx.MODE_PRIVATE);

        return getList(sp);
    }

    /**
     * Function to save a list of items in JSON format into the shared preference file
     * @param key - a key to save the data in the shared preference under
     * @param list - list of items to save
     * @param editor - the shared preference editor
     */
    public static  <T> void setList(String key, List<T> list, SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json, editor);
    }

    /**
     * Functions that sets a string into the shared preference file
     * @param key - key to save data with
     * @param value - the value to save
     * @param editor - the shared preference editor
     */
    public static void set(String key, String value, SharedPreferences.Editor editor) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * function to read a JSON saved in the shared preference file and parse it into an Arraylist
     * @param sp - shared preference file
     * @return - Arraylist of items loaded from the file
     */
    public static ArrayList<TopTenItem> getList(SharedPreferences sp){
        ArrayList<TopTenItem> arrayItems = new ArrayList<TopTenItem>();
        String serializedObject = sp.getString(KEY_PREFS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<TopTenItem>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
}
