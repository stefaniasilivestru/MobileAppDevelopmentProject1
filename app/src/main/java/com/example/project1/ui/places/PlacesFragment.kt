package com.example.project1.ui.places

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Place
import com.example.project1.PlaceAdapter
import com.example.project1.R
import com.example.project1.Route
import com.example.project1.RouteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlacesFragment : Fragment() {

    companion object {
        const val  DATABASE_URL = "https://tripify-d8b05-default-rtdb.europe-west1.firebasedatabase.app/"
    }

    var recyclerView : RecyclerView? = null
    private lateinit var placesList : ArrayList<Place>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.fragment_view_places, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_places)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // retrive the data from the database into a recycler view
        if (FirebaseAuth.getInstance().currentUser != null) {
           firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")
            Log.d("PlacesFragment", "Firebase reference: $firebaseRef")
            placesList = arrayListOf()
            fetchData()

        }



        return view

    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val sharedPreferences = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                    val routeId = sharedPreferences?.getString("routeId", null)
                    Log.d("PlacesFragment", "Route ID: $routeId")

                    if (routeId != null && snapshot.child(routeId).exists()) {
                        for (placeSnapshot in snapshot.child(routeId).child("places").children) {
                            val place = placeSnapshot.getValue(Place::class.java)
                            placesList.add(place!!)
                        }
                    } else {
                        Log.e("PlacesFragment", "Route ID is null or does not exist")
                    }
                }
                createRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun createRecyclerView() {
        recyclerView?.adapter = PlaceAdapter(placesList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

}