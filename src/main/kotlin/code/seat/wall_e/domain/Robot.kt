package code.seat.wall_e.domain

import code.seat.wall_e.shared.domain.geodata.GeoData.bearing
import code.seat.wall_e.shared.domain.geodata.GeoData.haversine
import com.google.maps.model.LatLng
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.*
import kotlin.random.Random

data class Robot(private val stations: List<Station>, private val name: String = "robot") {
    private val MINUTES_PER_REPORT = 15
    private val METERS_PER_SECOND = 3
    private val METERS_DISTANCE_PER_READ = 100.00
    private val totalStopsPerReport: Int = (MINUTES_PER_REPORT * 60) * 100 / (100 * 1 / METERS_PER_SECOND) / METERS_DISTANCE_PER_READ.toInt()

    private fun collectMeasures(totalStops: Int): List<Int> = (1..totalStops).map { Random.nextInt(0, 200) }

    private fun moveTowardsPoints(currentPoint: LatLng, nextPoint: LatLng, distance: Double): LatLng {
        val R = 6371000
        val currentLatRadians: Double = Math.toRadians(currentPoint.lat)
        val currentLngRadians: Double = Math.toRadians(currentPoint.lng)
        val nextLatRadians: Double = Math.toRadians(nextPoint.lat)
        val bearing: Double = bearing(currentPoint, nextPoint)
        val angDist: Double = distance / R
        val destinationLat: Double = asin(sin(currentLatRadians) * cos(angDist) + cos(currentLatRadians) * sin(angDist) * cos(bearing))
        val destinationLng: Double = currentLngRadians + atan2(sin(bearing) * sin(angDist) * cos(currentLatRadians), cos(angDist) - sin(currentLatRadians) * sin(nextLatRadians))

        return LatLng(Math.toDegrees(destinationLat), Math.toDegrees(destinationLng))
    }

    private fun moveAlongPath(points: List<LatLng>, distance: Double, index: Int = 0): LatLng? {
        if (index >= points.size - 1) {
            return null
        }

        val metersToNextPoint: Double = haversine(points[index], points[index + 1])
        return if (distance <= metersToNextPoint) moveTowardsPoints(points[index], points[index + 1], distance) else moveAlongPath(points, distance - metersToNextPoint, index + 1)
    }

    private fun collect(coordsFromPolyline: List<LatLng>, distance: Double): List<Report> {
        val measures: List<Int> = collectMeasures(totalStopsPerReport)
        val averagePM25Quality: PM25 = PM25.quality(measures.average().toInt())
        val measuredPoints: List<LatLng> = (0..totalStopsPerReport).mapNotNull { stop -> moveAlongPath(coordsFromPolyline, distance + (METERS_DISTANCE_PER_READ * stop)) }
        val stationReports: List<Report> = measuredPoints.flatMap { latLng -> stations.mapNotNull { station -> station.monitoring(latLng, averagePM25Quality) } }
        val robotReport = Report(
                Instant.now().toEpochMilli(),
                measuredPoints.last(),
                averagePM25Quality,
                name
        )

        return listOf(stationReports, listOf(robotReport)).flatten()
    }

    fun start(coordsFromPolyline: List<LatLng>): List<Report> {
        val totalMetersPath: Double = coordsFromPolyline.mapIndexed { index, latLng ->
            val distance: Double = if (index < coordsFromPolyline.size - 1) haversine(latLng, coordsFromPolyline[index + 1]) else 0.0
            distance
        }.sum()

        val totalJumpsPath: Int = floor(totalMetersPath / (totalStopsPerReport * METERS_DISTANCE_PER_READ)).toInt()

        return (0..totalJumpsPath).flatMap { stop ->
            val distance: Double = (totalStopsPerReport * METERS_DISTANCE_PER_READ) * stop
            collect(coordsFromPolyline, distance).map { report ->
                report.copy(
                        timestamp = Instant.now().plus((MINUTES_PER_REPORT.toLong() * (stop + 1)), ChronoUnit.MINUTES).toEpochMilli()
                )
            }
        }
    }
}
