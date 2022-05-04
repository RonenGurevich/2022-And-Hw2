package com.example.huntinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Utility.UtilityMethods;
import Fragments.TT_Fragment;

public class TopTenActivity extends FragmentActivity implements OnMapReadyCallback {

    TT_Fragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_ten);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.TT_FRG_map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        frag = (TT_Fragment)fm.findFragmentById(R.id.top_ten);
        Button backToMenu = findViewById(R.id.TT_BTN_ToMenu);

        backToMenu.setOnClickListener(view -> {
            finish();
            UtilityMethods.switchActivity(this, MenuActivity.class);
        });
    }

    /**
     * once an item on the top ten list is clicked, show the saved location on the map
     * put a marker there and move the camera
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        assert frag != null;
        frag.setOnTTItemClicked((item) -> {
            LatLng location = new LatLng(item.getLat(), item.getLon());
            googleMap.addMarker(new MarkerOptions().position(location).title("saved position"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        });

    }
}