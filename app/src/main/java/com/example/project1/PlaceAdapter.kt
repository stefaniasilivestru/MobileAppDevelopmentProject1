package com.example.project1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class PlaceAdapter(var placeList: ArrayList<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    companion object {
        const val  DATABASE_URL = "https://tripify-d8b05-default-rtdb.europe-west1.firebasedatabase.app/"
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.text_place_name)
        val longitude: TextView = view.findViewById(R.id.text_longitude)
        val latitude: TextView = view.findViewById(R.id.text_latitude)
        val deletePlaceButton : Button = view.findViewById(R.id.button_delete_place)
        val viewWeatherButton : Button = view.findViewById(R.id.button_view_weather)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = placeList[position]
        holder.placeName.text = currentItem.placeName
        holder.longitude.text = currentItem.longitude.toString()
        holder.latitude.text = currentItem.latitude.toString()

        holder.deletePlaceButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Delete place button clicked", Toast.LENGTH_SHORT).show()
//            val position = holder.adapterPosition
//            deletePlace(holder, position)
        }

        holder.viewWeatherButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "View weather button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePlace(holder: ViewHolder, position: Int) {
        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Delete Place")
            .setMessage("Are you sure you want to delete this place?")
            .setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(holder.itemView.context, "Delete cancelled", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("Delete") { dialog, which ->
                val firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")
                val sharedPreferences = holder.itemView.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                val routeId = sharedPreferences?.getString("routeId", null)
                Log.d("PlaceAdapter", "Route ID: $routeId")

                if (routeId != null) {
                    // Check if the position is valid
                    if (position in 0 until placeList.size) {
                        // Remove the place from the list at the specified position
                        val removedPlace = placeList.removeAt(position)

                        // Notify the adapter of the item removal
                        notifyItemRemoved(position)

                        // Construct the database reference to the place using routeId and placeId
                        val placeRef = firebaseRef.child(routeId).child(removedPlace.placeId!!)
                        Log.d("PlaceAdapter", "Place ID: ${removedPlace.placeId}")

                        // Delete the place from the database
                        placeRef.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(holder.itemView.context, "Place deleted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(holder.itemView.context, "Failed to delete place", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(holder.itemView.context, "Invalid position", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(holder.itemView.context, "Route ID is null", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }


}