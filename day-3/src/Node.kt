import kotlin.math.abs

class Node (val x : Int, val y : Int) {
    var next: Node? = null
    var isVisited = false
    var isJunction = false
    var stepsToHere = 0
    val manhattanDistance: Long = abs(x).toLong() + abs(y).toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as Node
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        // Szudzik's pairing function
        val a = if (x >= 0) 2 * x else -2 * x - 1
        val b = if (y >= 0) 2 * y else -2 * y - 1
        return if (a >= b) a * a + a + b else a + b * b
    }

    override fun toString(): String {
        return "($x, $y); -> $manhattanDistance"
    }
}