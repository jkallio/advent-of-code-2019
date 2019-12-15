package fi.jkallio

class Point (var x : Int, var y : Int) {
    var paintedWhite = false

    companion object {
        fun szudzikPairingFunction(x: Int, y: Int): Int{
            val a = if (x >= 0) 2 * x else -2 * x - 1
            val b = if (y >= 0) 2 * y else -2 * y - 1
            return if (a >= b) a * a + a + b else a + b * b
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as Point
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
       return Companion.szudzikPairingFunction(x, y)
    }

    override fun toString(): String {
        return "($x,$y)"
    }
}