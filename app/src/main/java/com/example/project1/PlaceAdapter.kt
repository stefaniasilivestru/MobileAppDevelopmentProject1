package com.example.project1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
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

    }
}