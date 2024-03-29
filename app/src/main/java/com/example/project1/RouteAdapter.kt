package com.example.project1

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class RouteAdapter(var routeList : List<Route>) : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {

    companion object {
        const val  DATABASE_URL = "https://tripify-d8b05-default-rtdb.europe-west1.firebasedatabase.app/"
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routeName: TextView = view.findViewById(R.id.text_route_name)
        val addPlaceButton: Button = view.findViewById(R.id.button_add_place)
        val deleteRouteButton : Button = view.findViewById(R.id.button_delete_route)
        val viewPlacesButton : Button = view.findViewById(R.id.button_view_places)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = routeList[position]
        holder.routeName.text = currentItem.routeName
        holder.addPlaceButton.setOnClickListener {
            addPlace(holder, currentItem)
        }
        holder.deleteRouteButton.setOnClickListener {
            deleteRoute(holder, currentItem)
        }
        holder.viewPlacesButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "View places button clicked", Toast.LENGTH_SHORT).show()
            findNavController(holder.itemView).navigate(R.id.action_routes_to_places)
            val sharedPreferences = holder.itemView.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("routeId", currentItem.routeId)
                apply()
            }
            Log.d("RouteAdapter", "Route ID: ${currentItem.routeId}")

        }
    }

    private fun addPlace(
        holder: ViewHolder,
        currentItem: Route
    ) {
        val placeNameEditText = EditText(holder.itemView.context)
        val longitudeEditText = EditText(holder.itemView.context)
        val latitudeEditText = EditText(holder.itemView.context)

        placeNameEditText.hint = "Place Name"
        longitudeEditText.hint = "Longitude"
        latitudeEditText.hint = "Latitude"

        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Add Place")
            .setIcon(R.drawable.logo_tripify)
            .setView(LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.VERTICAL
                addView(placeNameEditText)
                addView(longitudeEditText)
                addView(latitudeEditText)
            })

            .setPositiveButton("Add") { dialog, which ->
                addPlaceToDatabase(
                    placeNameEditText,
                    longitudeEditText,
                    latitudeEditText,
                    holder,
                    currentItem
                )

            }
            .setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(holder.itemView.context, "Add place cancelled", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }
            .show()
    }

    private fun addPlaceToDatabase(
        placeNameEditText: EditText,
        longitudeEditText: EditText,
        latitudeEditText: EditText,
        holder: ViewHolder,
        currentItem: Route
    ) {
        val placeName = placeNameEditText.text.toString()
        val longitude = longitudeEditText.text.toString().toDoubleOrNull()
        val latitude = latitudeEditText.text.toString().toDoubleOrNull()

        if (placeName.isEmpty() || longitude == null || latitude == null) {
            Toast.makeText(holder.itemView.context, "Please enter valid values", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")

        val places = currentItem.places?.toMutableList()
        val placeId = firebaseRef.push().key

        val newPlace = Place(
            placeId!!,
            placeName = placeName,
            longitude = longitude,
            latitude = latitude
        )
        places?.add(newPlace)

        // update the route in the database
        firebaseRef.child(currentItem.routeId.toString()).child("places").setValue(places)
            .addOnSuccessListener {
                Toast.makeText(holder.itemView.context, "Place added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(holder.itemView.context, "Failed to add place", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun deleteRoute(
        holder: ViewHolder,
        currentItem: Route
    ) {
        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Delete Route")
            .setMessage("Are you sure you want to delete this route?")
            .setNegativeButton("No") { dialog, which ->
                Toast.makeText(holder.itemView.context, "Delete cancelled", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, which ->
                // delete from the database
                val firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")
                firebaseRef.child(currentItem.routeId.toString()).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Route deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "Failed to delete route",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .show()
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

}