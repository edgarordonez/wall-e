package code.seat.wall_e.domain

enum class PM25 {
    GOOD,
    MODERATE,
    USG,
    UNHEALTHY,
    UNKNOWN;

    companion object {
        fun quality(measure: Int): PM25 {
            return when {
                measure <= 50 -> GOOD
                measure in 51..100 -> MODERATE
                measure in 51..150 -> USG
                measure > 151 -> UNHEALTHY
                else -> UNKNOWN
            }
        }
    }
}
