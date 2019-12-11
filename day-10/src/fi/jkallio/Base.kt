package fi.jkallio

class Base(val location: Point) {
    var detectedAsteroids = 0

    // Returns map of vectors pointing to detected asteroids (key=vector magnitude)
    fun scanAsteroids(asteroids: List<Point>): Map<Double, Vector> {
        val radarRays = mutableMapOf<Double, Vector>()
        asteroids.forEach { asteroidLocation ->
            if (location != asteroidLocation) {
                val v1 = Vector(location, asteroidLocation)
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
        return radarRays
    }
}