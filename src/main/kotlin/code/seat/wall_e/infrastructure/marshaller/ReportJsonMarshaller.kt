package code.seat.wall_e.infrastructure.marshaller

import code.seat.wall_e.domain.Report
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

object ReportJsonMarshaller {
    val reportConverter = object : Converter {
        override fun canConvert(cls: Class<*>) = cls == Report::class.java

        override fun fromJson(jv: JsonValue): Any? = TODO("Not yet implemented")

        override fun toJson(value: Any): String {
            val report = value as Report
            return """{"timestamp": ${report.timestamp},"location": {"lat": ${report.location.lat}, "lng": ${report.location.lng}},"level": "${report.level}","source": "${report.source}"}""".trimMargin()
        }
    }
}
