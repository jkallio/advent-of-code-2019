package fi.jkallio

class Vector3(var x: Int, var y: Int, var z: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as Vector3
        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}