//Crainie_Michael_S1828588
package org.me.gcu.equakestartercode;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap map;
    private Button backButton;
    private RadioGroup radioGroup;
    private RadioButton normal;
    private RadioButton terrain;
    private RadioButton satellite;
    private RadioButton hybrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapfragment);
        backButton = (Button) findViewById(R.id.backButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        normal = (RadioButton) findViewById(R.id.standardButton);
        terrain = (RadioButton) findViewById(R.id.terrainButton);
        satellite = (RadioButton) findViewById(R.id.satButton);
        hybrid = (RadioButton) findViewById(R.id.hybridButton);
        backButton.setOnClickListener(this);
        normal.setOnClickListener(this);
        terrain.setOnClickListener(this);
        satellite.setOnClickListener(this);
        hybrid.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(55, 3.4));
        CameraUpdateFactory.zoomTo(10);
        map.moveCamera(point);
        for(int i = 0; i < InfoHolder.itemLatitude.size(); i++) {
            LatLng mapMarker = new LatLng(Double.valueOf(InfoHolder.itemLatitude.get(i)), (Double.valueOf(InfoHolder.itemLongitude.get(i))));

            if(Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10  < 1)
            {
                map.addMarker(new MarkerOptions().position(mapMarker).title(InfoHolder.itemTitles.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }

            if(Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10  >= 1 && Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10 <= 1.9)
            {
                map.addMarker(new MarkerOptions().position(mapMarker).title(InfoHolder.itemTitles.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }

            if(Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10  >= 2 && Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10 <= 2.9)
            {
                map.addMarker(new MarkerOptions().position(mapMarker).title(InfoHolder.itemTitles.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }

            if(Double.valueOf(InfoHolder.itemMagnitude.get(i)) /10  >= 3)
            {
                map.addMarker(new MarkerOptions().position(mapMarker).title(InfoHolder.itemTitles.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }

        }
    }

    public void MoveCamera()
    {

    }

    @Override
    public void onClick(View v) {
        if(v == backButton)
        {
            map.clear();
            InfoHolder.itemDescriptions.clear();
            InfoHolder.itemFullInfo.clear();
            InfoHolder.itemLatitude.clear();
            InfoHolder.itemLongitude.clear();
            InfoHolder.itemTitles.clear();
            InfoHolder.itemMagnitude.clear();
            //setContentView(R.layout.activity_main);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if(v == normal)
        {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        if(v == terrain)
        {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        if(v == satellite)
        {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        if(v == hybrid)
        {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }
}
