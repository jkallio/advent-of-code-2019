package fi.jkallio

class Droid (private val id: Int,
             private val intcode: Intcode,
             private val shipMap: MutableMap<Int, Point>,
             var direction: Direction,
             var position: Point = Point(0,0),
             val route: MutableList<Point> = mutableListOf()) {

    enum class Direction { NORTH, SOUTH, WEST, EAST }
    var foundOxygenSystem = false
    var isDead = false

    init {
        addRoutePoint(position)
    }

    private fun addRoutePoint(point: Point) {
        if (!route.contains(point)) {
            route.add(point)
        }
        position = point
        if (point.x == 0 && point.y == 0) {
            point.tile = Tile.START
        }
        shipMap.putIfAbsent(point.hashCode(), point)
    }

    private fun directionToInt(dir: Direction): Int {
        return when (dir) {
            Direction.NORTH -> 1
            Direction.SOUTH -> 2
            Direction.WEST  -> 3
            Direction.EAST  -> 4
        }
    }

    fun move() {
        val x = if (direction == Direction.EAST) 1 else if (direction == Direction.WEST) -1 else 0
        val y = if (direction == Direction.NORTH) 1 else if (direction == Direction.SOUTH) -1 else 0
        val point = Point(position.x + x, position.y + y)
        if (!shipMap.containsKey(point.hashCode())) {
            when (intcode.run(directionToInt(direction))) {
                0 -> {
                    point.tile = Tile.WALL
                    println("#$id: Hit a wall at $point")
                    isDead = true
                }
                1 -> {
                    point.tile = Tile.EMPTY
                    println("#$id: Moved to $position (${route.count()} steps)")
                    foundOxygenSystem = false
                }
                2 -> {
                    point.tile = Tile.OXYGEN_SYSTEM
                    println("#$id: Moved to $position (${route.count()} steps) --> *** Oxygen system found ***")
                    foundOxygenSystem = true
                }
            }
            addRoutePoint(point)
        }
        else {
            // Another droid already visited here
            println("#$id: entered already visited node at $point")
            isDead = true
        }
    }

    fun cloneWithDirection(id: Int, dir: Direction) : Droid {
        val r = mutableListOf<Point>()
        route.forEach {
            r.add(Point(it.x, it.y, it.tile))
        }
        return Droid(id, intcode.clone(), shipMap, dir, position, r)
    }
}