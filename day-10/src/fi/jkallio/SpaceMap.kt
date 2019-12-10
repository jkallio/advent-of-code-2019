package fi.jkallio

class SpaceMap(input: List<String>) {
    val width: Int
    val height: Int
    val nodes: Map<Int, Node>

    init {
        var tmp = mutableMapOf<Int, Node>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                val node = Node(x, y)
                if (input[y][x] == '#') {
                    node.isAsteroid = true
                }
                tmp[node.hashCode()] = node
            }
        }
        width = input.first().length
        height = input.count()
        nodes = tmp
    }
}