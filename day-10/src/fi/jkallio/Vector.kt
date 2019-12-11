package fi.jkallio

import java.lang.Math.*
import kotlin.math.sqrt

class Vector (val p: Point, val q: Point) {
    val x: Int
    val y: Int
    val length: Double
    val angle: Double

    init {
        // Find the vector and angle
        x = q.x - p.x
        y = q.y - p.y
        length = sqrt(pow(x.toDouble(), 2.0) + pow(y.toDouble(), 2.0))
        angle = Math.toDegrees(kotlin.math.atan2(x.toDouble(), y.toDouble()))
    }

    override fun toString(): String {
        return ("$q; L=${"%.1f".format(length)}; A=${"%.1f".format(angle)}")
    }
}