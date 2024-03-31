package com.example.project1.ui.routes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project1.PlaceAdapter
import com.example.project1.R
import com.example.project1.databinding.FragmentMapBinding
import com.example.project1.databinding.FragmentViewRouteBinding
import com.google.firebase.database.FirebaseDatabase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class ViewRouteFragment : Fragment() {

    companion object {
        const val  DATABASE_URL = "https://tripify-d8b05-default-rtdb.europe-west1.firebasedatabase.app/"
    }

    private var _binding: FragmentViewRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewRouteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        map = binding.mapRoute
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(15.0)

        // Get the specific route from the database
        val firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")
        val sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val routeId = sharedPreferences?.getString("routeId", null)
        Log.d("ViewRouteFragment", "Route ID: $routeId")

        // Get the places of the route from the database
        val places = firebaseRef.child(routeId.toString()).child("places")
        val placeListGeoPoint = mutableListOf<GeoPoint>()
        val placeListNames = mutableListOf<String>()

        places.get().addOnSuccessListener {
            for (place in it.children) {
                val placeName = place.child("placeName").value.toString()
                val latitude = place.child("latitude").value.toString().toDouble()
                val longitude = place.child("longitude").value.toString().toDouble()
                val placeGeoPoint = GeoPoint(latitude, longitude)
                placeListGeoPoint.add(placeGeoPoint)
                placeListNames.add(placeName)
            }
            val startPoint = placeListGeoPoint[0]
            Log.d("ViewRouteFragment", "Start point: $startPoint")
            map.controller.setCenter(startPoint)
            addMarkersAndRoute(map, placeListGeoPoint, placeListNames)
        }

        return root

    }
    fun addMarkersAndRoute(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {
        if (locationsCoords.size != locationsNames.size) {
            Log.e("addMarkersAndRoute", "Locations and names lists must have the same number of items.")
            return
        }
        val route = Polyline()
        route.setPoints(locationsCoords)
        route.color = ContextCompat.getColor(requireContext(), R.color.teal_700)
        mapView.overlays.add(route)
        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            val locationIndex = locationsCoords.indexOf(location)
            marker.title = "${locationsNames[locationIndex]}"
            marker.icon = ContextCompat.getDrawable(requireContext(), org.osmdroid.library.R.drawable.marker_default)
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

}