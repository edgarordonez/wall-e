package code.seat.wall_e.shared.domain.geodata

import com.google.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object GeoData {
    fun haversine(currentPoint: LatLng, nextPoint: LatLng): Double {
        val R = 6371e3 // in meters
        val lat1: Double = currentPoint.lat
        val lon1: Double = currentPoint.lng
        val lat2: Double = nextPoint.lat
        val lon2: Double = nextPoint.lng
        val latDistance: Double = Math.toRadians(lat2 - lat1)
        val lonDistance: Double = Math.toRadians(lon2 - lon1)
        val a: Double = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c: Double = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }

    fun bearing(currentPoint: LatLng, nextPoint: LatLng): Double {
        val λ1: Double = Math.toRadians(currentPoint.lat)
        val λ2: Double = Math.toRadians(nextPoint.lat)
        val Δφ: Double = Math.toRadians(nextPoint.lng - currentPoint.lng)
        return atan2(sin(Δφ) * cos(λ2), cos(λ1) * sin(λ2) - sin(λ1) * cos(λ2) * cos(Δφ))
    }
}
