import java.lang.NullPointerException
import kotlin.Exception

class Wire (input: String) {
    val nodes = mutableMapOf<Int, Node>()
    init {
        var prevNode = Node(0,0)
        addNode(prevNode)
        input.split(',').forEach {
            try {
                val dir = it[0]
                val steps = it.substring(1).toInt()
                for (i in 0 until steps) {
                    val node = when (dir) {
                        'U' -> { Node(prevNode.x, prevNode.y + 1) }
                        'D' -> { Node(prevNode.x, prevNode.y - 1) }
                        'R' -> { Node(prevNode.x + 1, prevNode.y) }
                        'L' -> { Node(prevNode.x - 1, prevNode.y) }
                        else -> { throw Exception("Invalid input direction") }
                    }
                    prevNode.next = node
                    prevNode = node
                    addNode(node)
                }
            } catch (e: Exception) {
                println("*** Exception: Failed to parse Wire input");
            }
        }
    }

    private fun addNode(node: Node) {
        nodes[node.hashCode()] = node
    }

    // Finds closest junction to Origo and marks the nodes as "junctions"
    fun findClosestJunction(other: Wire): Long {
        var closest = Long.MAX_VALUE
        nodes.forEach { it ->
            if (it.value.x != 0 && it.value.y != 0 && other.nodes.containsKey(it.key)) {
                it.value.isJunction = true
                other.nodes[it.key]?.isJunction = true
                if  (it.value.manhattanDistance < closest) {
                    closest = it.value.manhattanDistance
                }
            }
        }
        return closest
    }

    // Walks the wire one node at a time while counting the steps
    // If the Wire has a intersection with itself (Node already visited) the step count is reset to this Node.
    fun countStepsToJunctions(): Map<Int, Node> {
        val junctions = mutableMapOf<Int, Node>()
        nodes[Node(0, 0).hashCode()].let { root ->
            var node: Node? = root?.next
            var steps = 0
            while (node != null) {
                // Reset step counter to here if visited before (to get the shortest amount of steps)
                steps = if (node.isVisited) node.stepsToHere else steps + 1

                if (node.isJunction) {
                    junctions[node.hashCode()] = node
                }
                node.isVisited = true
                node.stepsToHere = steps
                node = node.next
            }
        }
        return junctions
    }
}