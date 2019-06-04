package com.tongameyerogatman.logintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*
import android.util.Log
import com.google.android.libraries.places.api.Places


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)

        // Initialize Places.
        Places.initialize(this@MapsActivity, getString(R.string.google_map_api_key))
        // Create a new Places client instance.
        val placesClient = Places.createClient(this@MapsActivity)

        val placeAutocompleteFragment = supportFragmentManager.findFragmentById(R.id.placeAutocompleteFragment) as AutocompleteSupportFragment
        placeAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                marker.title = place?.name.toString()
                marker.position = place?.latLng
                map.moveCamera(CameraUpdateFactory.newLatLng(place?.latLng))
            }

            override fun onError(status: Status) {
                Log.e("meyer", status.statusMessage)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val startingPoint = LatLng(2.5938825, 98.6980123)
        marker = map.addMarker(MarkerOptions().position(startingPoint).title("Samosir"))

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 12f))
    }
}
