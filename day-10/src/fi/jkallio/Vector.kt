package fi.jkallio

import kotlin.math.pow
import kotlin.math.sqrt

class Vector (val p: Point, val q: Point) {
    private val direction = Pair(
        q.x.toDouble() - p.x.toDouble(),
        q.y.toDouble() - p.y.toDouble())
    val magnitude: Double
    val angle: Double

    init {
        magnitude = sqrt(direction.first.pow(2.0) + direction.second.pow(2.0))
        angle = Math.toDegrees(kotlin.math.atan2(direction.first, direction.second))
    }

    override fun toString(): String {
        return ("$q; L=${"%.1f".format(magnitude)}; A=${"%.1f".format(angle)}")
    }
}