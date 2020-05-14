package code.seat.wall_e.domain

import com.google.maps.model.LatLng

data class Report(val timestamp: Long, val location: LatLng, val level: PM25, val source: String = "robot")