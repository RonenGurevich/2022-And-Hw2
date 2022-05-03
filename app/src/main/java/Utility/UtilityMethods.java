package Utility;

import android.Manifest;
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

    public static void switchActivity(Context context, Class dest) {
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
    }

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

    public static void saveTopTen(Context ctx, List<TopTenItem> items) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        setList(KEY_PREFS, items, editor);
    }

    public static ArrayList<TopTenItem> loadTopTen(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_NAME, ctx.MODE_PRIVATE);

        return getList(sp);
    }

    public static  <T> void setList(String key, List<T> list, SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json, editor);
    }

    public static void set(String key, String value, SharedPreferences.Editor editor) {
        editor.putString(key, value);
        editor.commit();
    }

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
