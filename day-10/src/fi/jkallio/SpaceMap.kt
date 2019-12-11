package fi.jkallio

class SpaceMap(input: List<String>) {
    val asteroids = mutableMapOf<Int, Point>()

    init {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '#') {
                    val point = Point(x, y)
                    asteroids[point.hashCode()] = point
                }
            }
        }
    }

    fun calculateDetectedAsteroids(p: Point): Int {
        val radarRays = mutableMapOf<Double, Vector>()
        asteroids.values.forEach { q ->
            if (p != q) {
                val v1 = Vector(p, q)
                if (radarRays.containsKey(v1.angle)) {
                    radarRays[v1.angle]?.let { v2 ->
                        if (v1.length < v2.length) {
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