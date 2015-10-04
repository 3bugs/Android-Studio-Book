package com.example.mapdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng ROT_FAI_PARK = new LatLng(13.810, 100.552);  // พิกัดสวนรถไฟ
    private static final LatLng PROVISION = new LatLng(13.783, 100.546);     // พิกัดโปรวิชั่น

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // สร้างหมุดสำหรับสวนรถไฟ กำหนดค่าต่างๆของหมุด แล้วเพิ่มหมุดลงบนแผนที่
        MarkerOptions moRotFaiPark = new MarkerOptions();
        moRotFaiPark.position(ROT_FAI_PARK);
        moRotFaiPark.title("สวนวชิรเบญจทัศ (สวนรถไฟ)");
        map.addMarker(moRotFaiPark);

        // สร้างหมุดสำหรับออฟฟิศของโปรวิชั่น กำหนดค่าต่างๆของหมุด แล้วเพิ่มหมุดลงบนแผนที่
        MarkerOptions moProvision = new MarkerOptions();
        moProvision.position(PROVISION);
        moProvision.title("บริษัท โปรวิชั่น จำกัด");
        moProvision.snippet("ชั้น 9 อาคารพหลโยธิน เพลส");
        moProvision.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        map.addMarker(moProvision);

        // ย้ายกล้องไปที่สวนรถไฟ และกำหนดการซูมระดับ 5
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ROT_FAI_PARK, 5));
        // ซูมมาที่ระดับ 13 แบบมีแอนิเมชั่น
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
    }
}
