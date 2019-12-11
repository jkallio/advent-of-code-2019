package fi.jkallio

class Base(val location: Point) {
    var detectedAsteroids = 0

    fun calculateDetectedAsteroids(asteroids: List<Point>): Int {
        val radarRays = mutableMapOf<Double, Vector>()
        asteroids.forEach { asteroid ->
            if (location != asteroid) {
                val v1 = Vector(location, asteroid)
                if (radarRays.containsKey(v1.angle)) {
                    radarRays[v1.angle]?.let { v2 ->
                        if (v1.magnitude < v2.magnitude) {
                            radarRays[v1.angle] = v1
                        }
                    }
                }
                else {
                    radarRays[v1.angle] = v1
                }
            }
        }
        return radarRays.count()
    }
}