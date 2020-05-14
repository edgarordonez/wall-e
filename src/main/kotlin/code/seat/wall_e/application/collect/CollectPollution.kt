package code.seat.wall_e.application.collect

import code.seat.wall_e.domain.Report
import code.seat.wall_e.domain.Robot
import code.seat.wall_e.domain.Station
import com.google.maps.model.EncodedPolyline
import com.google.maps.model.LatLng
import java.lang.Exception

object CollectPollution {
    fun collect(polyline: String): List<Report>? {
        return try {
            val coordsFromPolyline: List<LatLng> = EncodedPolyline(polyline).decodePath()
            val stations: List<Station> = listOf(
                    Station("Buckingham Palace", LatLng(51.501299, -0.141935)),
                    Station("Temple Station", LatLng(51.510852, -0.114165))
            )

            Robot(stations).start(coordsFromPolyline)
        } catch (e: Exception) {
            println("Sorry, there was a problem: ${e} with the polyline.")
            null
        }
    }
}