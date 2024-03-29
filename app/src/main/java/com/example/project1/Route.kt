package com.example.project1

data class Route(
    val routeId: String? = "",
    val routeName: String? = "",
    var places: List<Place>? = emptyList()
)




