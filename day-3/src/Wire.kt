import java.lang.NullPointerException
import kotlin.Exception

class Wire (private val inputString: String) {
    val nodes = mutableListOf<Node>()
    private var lastNode : Node = Node(0,0)

    init {
        parseInputString(this.inputString)
    }

    fun addNode(node: Node) {
        nodes.add(node)
        lastNode = node
    }

    fun parseInputString(input: String) {
        input.split(',').forEach {
            if (it.length > 1) {
                try {
                    val dir = it[0]
                    val steps = it.substring(1).toInt()

                    for (i in 0 until steps) {
                        val node : Node = when (dir) {
                            'U' -> { Node(lastNode.x, lastNode.y + 1) }
                            'D' -> { Node(lastNode.x, lastNode.y - 1) }
                            'R' -> { Node(lastNode.x + 1, lastNode.y) }
                            'L' -> { Node(lastNode.x - 1, lastNode.y) }
                            else -> { throw Exception("Invalid input direction") }
                        }
                        addNode(node)
                    }
                } catch (e: Exception) {
                    println("*** Exception: Failed to parse Wire input");
                }
            }
        }
    }
}