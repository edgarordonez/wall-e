package shared.domain

import code.seat.wall_e.shared.domain.geodata.GeoData
import com.google.maps.model.LatLng
import org.junit.Assert.*
import org.spekframework.spek2.Spek

object GeoDataSpec : Spek({
    group("GeoData") {
        test("haversine should return the correct distance in meters between two points") {
            val point1 = LatLng(51.5109900, -0.1142280)
            val point2 = LatLng(51.5119999, -0.1154239)
            val distance: Double = GeoData.haversine(point1, point2)
            val expectedDistance = 139.49763226578

            assertEquals(expectedDistance, distance, 0.2)
        }

        test("bearing should return the correct initial bearing between two points") {
            val point1 = LatLng(51.5109900, -0.1142280)
            val point2 = LatLng(51.5119999, -0.1154239)
            val expectedBearing = -0.6351088102561656
            val bearing = GeoData.bearing(point1, point2)

            assertEquals(expectedBearing, bearing, 0.0)
        }
    }
})
