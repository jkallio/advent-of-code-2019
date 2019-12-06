import kotlin.math.abs

class Node (val x : Int, val y : Int) {
    val manhattanDistance : Int = abs(x) + abs(y)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as Node
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "${super.toString()}: ($x, $y) -> $manhattanDistance"
    }
}