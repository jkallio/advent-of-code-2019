package fi.jkallio
import kotlin.math.abs

class Moon (val name: String,
            var position: Vector3,
            var velocity: Vector3 = Vector3(0,0,0)) {

    private val initPosition = Vector3(position.x, position.y, position.z)
    private val initVelocity = Vector3(velocity.x, velocity.y, velocity.z)

    fun energy(): Int {
        val a = abs(position.x) + abs(position.y) + abs(position.z)
        val b = abs(velocity.x) + abs(velocity.y) + abs(velocity.z)
        return a * b
    }

    fun updatePosition() {
        position.x += velocity.x
        position.y += velocity.y
        position.z += velocity.z
    }

    fun applyGravity(other: Moon) {
        velocity.x += when {
            position.x < other.position.x -> 1
            position.x > other.position.x -> -1
            else -> 0
        }
        velocity.y += when {
            position.y < other.position.y -> 1
            position.y > other.position.y -> -1
            else -> 0
        }
        velocity.z += when {
            position.z < other.position.z -> 1
            position.z > other.position.z -> -1
            else -> 0
        }
    }

    fun isInitState(): Boolean {
        return position == initPosition && velocity == initVelocity
    }

    override fun toString(): String {
        return "<${position.x},${position.y},${position.z}> <${velocity.x},${velocity.y},${velocity.z}> ($name)"
    }
}