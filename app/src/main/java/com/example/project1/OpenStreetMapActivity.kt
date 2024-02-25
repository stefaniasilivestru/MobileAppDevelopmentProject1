package com.example.project1

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class OpenStreetMapActivity : AppCompatActivity() {
    private val TAG = "location"
    private lateinit var map: MapView

    val coords = listOf(
        GeoPoint(40.4063, -3.6901), // Atocha Railway Station
        GeoPoint(40.4079, -3.6945), // Reina Sofia Museum
        GeoPoint(40.4139, -3.6923),  // Prado Museum
        GeoPoint(40.4145, -3.6919), // Botanical Garden
        GeoPoint(40.4087, -3.6910) // Ministerio de Agricultura
    )

    val names = listOf(
        "Atocha Railway Station",
        "Reina Sofia Museum",
        "Prado Museum",
        "Botanical Garden",
        "Ministerio de Agricultura"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val buttonToMain : Button = findViewById(R.id.buttonMapToMain)
        buttonToMain.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        if (location != null) {
            Log.i(
                TAG,
                "onCreate: Location[" + location.altitude + "][" + location.latitude + "][" + location.longitude + "]["
            )
            Configuration.getInstance()
                .load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
            map = findViewById(R.id.map)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(16.0)
            val startPoint = GeoPoint(location.latitude, location.longitude)
            // val startPoint = GeoPoint(40.416775, -3.703790) in case you want to test it mannualy
            map.controller.setCenter(startPoint)

            addMarker(startPoint, "Start Point")
            Log.i("Marker", addMarker(startPoint, "Start Point").toString())

            addMarkers(map, coords, names)
        }
    }

    private fun addMarker(point: GeoPoint, title: String) {
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        map.overlays.add(marker)
        map.invalidate() // Reload map
    }

    // Add markers and route too
    private fun addMarkers(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {

        if (locationsCoords.size != locationsNames.size) {
            Log.e("addMarkersAndRoute", "Locations and names lists must have the same number of items.")
            return
        }

        val route = Polyline()
        route.setPoints(locationsCoords)
        route.color = ContextCompat.getColor(this, R.color.red)
        mapView.overlays.add(route)

        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Marker at " + locationsNames[locationsCoords.indexOf(location)]
            marker.icon = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.ic_m3_chip_checked_circle)
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refresh the map to display the new markers
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}