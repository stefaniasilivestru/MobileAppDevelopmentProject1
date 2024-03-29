package com.example.project1.ui.routes

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Place
import com.example.project1.R
import com.example.project1.Route
import com.example.project1.RouteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RoutesFragment : Fragment() {

    companion object {
        const val  DATABASE_URL = "https://tripify-d8b05-default-rtdb.europe-west1.firebasedatabase.app/"
    }

    var buttonAddRoute : FloatingActionButton? = null
    var textRoutes : TextView? = null
    var recyclerView : RecyclerView? = null
    private lateinit var routesList : ArrayList<Route>
    private lateinit var firebaseRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view : View = inflater.inflate(R.layout.fragment_routes_connected, container, false)
        textRoutes = view.findViewById(R.id.text_routes)
        buttonAddRoute = view.findViewById(R.id.button_add_route)
        recyclerView = view.findViewById(R.id.recycler_view_routes)
        recyclerView?.layoutManager = LinearLayoutManager(context)

            if (FirebaseAuth.getInstance().currentUser != null) {
                firebaseRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("routes")
                routesList = arrayListOf()
                buttonAddRoute!!.setOnClickListener() {
                    addRoute()
                }

                // retrive the data from the database into a recycler view
                fetchData()
                Log.d("RoutesFragment", routesList.toString())


            } else {
                view = inflater.inflate(R.layout.fragment_routes_unconnected, container, false)
            }

            return view

        }

    private fun addRoute() {
        val routeName = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Add a new route")
            .setIcon(R.drawable.logo_tripify)
            .setView(routeName)
            .setPositiveButton("Add") { dialog, which ->
                val route = routeName.text.toString()
                if (route.isEmpty()) {
                    Toast.makeText(context, "Please enter a route name", Toast.LENGTH_SHORT).show()
                } else {
                    val routeId = firebaseRef.push().key
    //                                val place = Place("1", "aaa", 1.2, 1.2)
                    val places = mutableListOf<Place>()
    //                                places.add(place)
                    val routes = Route(routeId!!, route, places)

                    firebaseRef.child(routeId!!).setValue(routes)

    //                                val place2 = Place("2", "bbb", 2.2, 2.2)
    //                                places.add(place2)
    //
    //                                firebaseRef.child(routeId!!).child("places").setValue(places)
                    Toast.makeText(context, "Route added", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    routesList.clear()
                    for (routeSnapshot in snapshot.children) {
                        val route = routeSnapshot.getValue(Route::class.java)
                        routesList.add(route!!)
                    }
                }
                createRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RoutesFragment", "Error getting data", error.toException())
            }
        })
    }

    private fun createRecyclerView() {
        recyclerView?.adapter = RouteAdapter(routesList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}