package fi.jkallio
import kotlin.math.abs

class Moon (private val name: String,
            private var position: Vector3) {

    private var velocity: Vector3 = Vector3(0, 0, 0)
    private val initPosition = Vector3(position.x, position.y, position.z)

    fun reset() {
        position = Vector3(initPosition.x, initPosition.y, initPosition.z)
        velocity = Vector3(0, 0, 0)
    }

    fun energy(): Int {
        val a = abs(position.x) + abs(position.y) + abs(position.z)
        val b = abs(velocity.x) + abs(velocity.y) + abs(velocity.z)
        return a * b
    }

    fun updatePosition(d: Int) {
        position.x += if (d == 0 || d == -1) velocity.x else 0
        position.y += if (d == 1 || d == -1) velocity.y else 0
        position.z += if (d == 2 || d == -1) velocity.z else 0
    }

    fun applyGravity(other: Moon, d: Int) {
        if (d == 0 || d == -1) {
            velocity.x += when {
                position.x < other.position.x -> 1
                position.x > other.position.x -> -1
                else -> 0
            }
        }
        if (d == 1 || d == -1) {
            velocity.y += when {
                position.y < other.position.y -> 1
                position.y > other.position.y -> -1
                else -> 0
            }
        }
        if (d == 2 || d == -1) {
            velocity.z += when {
                position.z < other.position.z -> 1
                position.z > other.position.z -> -1
                else -> 0
            }
        }
    }

    fun isInitState(d: Int): Boolean {
        return when (d) {
            0 -> position.x == initPosition.x && velocity.x == 0
            1 -> position.y == initPosition.y && velocity.y == 0
            2 -> position.z == initPosition.z && velocity.z == 0
            else -> false
        }
    }

    override fun toString(): String {
        return "<${position.x},${position.y},${position.z}> <${velocity.x},${velocity.y},${velocity.z}> ($name)"
    }
}