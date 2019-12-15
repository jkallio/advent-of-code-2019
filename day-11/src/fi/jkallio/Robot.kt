package fi.jkallio

class Robot(val intcode: Intcode) {
    enum class EDirection { UP, DOWN, LEFT, RIGHT }
    private var facing = EDirection.UP
    private var position = Point(0,0)
    private val hullMap = mutableMapOf<Int, Point>()
    private var topLeft = Point(0,0)
    private var botRight = Point(0,0)

    init {
        hullMap[position.hashCode()] = position
    }

    fun startPainting() {
        while (!intcode.isFinished()) {

            // Check current panel color and paint
            val currentPanelColor = if (hullMap[position.hashCode()]!!.paintedWhite) 1 else 0
            val paintColor = intcode.run(currentPanelColor.toLong()).toInt()
            position.paintedWhite = paintColor == 1

            // Turn direction based on output and move one square to that direction
            val outDir = intcode.run().toInt()
            move(outDir != 0)
        }
        println("Boundaries = $topLeft; $botRight")
        println("Painted panels = ${ hullMap.count() }")
    }

    private fun move(turnRight: Boolean) {
        facing = when (facing) {
            EDirection.UP       -> { if (turnRight) EDirection.RIGHT else EDirection.LEFT }
            EDirection.RIGHT    -> { if (turnRight) EDirection.DOWN else EDirection.UP }
            EDirection.DOWN     -> { if (turnRight) EDirection.LEFT else EDirection.RIGHT }
            EDirection.LEFT     -> { if (turnRight) EDirection.UP else EDirection.DOWN }
        }

        val newPos = when(facing) {
            EDirection.UP       -> Point(position.x, position.y+1)
            EDirection.RIGHT    -> Point(position.x+1, position.y)
            EDirection.DOWN     -> Point(position.x, position.y-1)
            EDirection.LEFT     -> Point(position.x-1, position.y)
        }
        position = hullMap.getOrPut(newPos.hashCode(), { newPos })

        // Update boundaries
        if (position.x < topLeft.x) topLeft.x = position.x
        if (position.y > topLeft.y) topLeft.y = position.y
        if (position.x > botRight.x) botRight.x = position.x
        if (position.y < botRight.y) botRight.y = position.y
    }
}