package code.seat.wall_e.domain

import code.seat.wall_e.shared.domain.geodata.GeoData.haversine
import com.google.maps.model.LatLng
import java.time.Instant

data class Station(val name: String, val latLng: LatLng, var reported: Boolean = false) {
    fun monitoring(latLng: LatLng, PM25: PM25): Report? {
        val distance: Double = haversine(this.latLng, latLng)

        if (reported || distance > 100.00) {
            return null
        }

        this.reported = true
        return Report(
                Instant.now().toEpochMilli(),
                latLng,
                PM25,
                name
        )
    }
}