package fi.jkallio

import java.lang.Exception

class GameState {
    val screen = mutableMapOf<Int, Point>()
    var score = 0           // Player score
    var paddlePos = 0       // Current paddle position
    var ballPos = 0         // Follow the ball position
    var nextInput = 0       // Where to move paddle next (follow the ball position)

    fun updateTile(point: Point) {
        screen[point.hashCode()] = point

        when (point.tile) {
            3 -> paddlePos = point.x
            4 -> ballPos = point.x
        }
        nextInput = if (paddlePos > ballPos) -1 else if (paddlePos < ballPos) 1 else 0
    }

    fun print() {
        val topLeft = screen.findTopLeft()
        val botRight = screen.findBottomRight()
        var str = ""

        try {
            for (y in topLeft.y downTo botRight.y) {
                for (x in topLeft.x until botRight.x) {
                    val point = screen.getValue(Point.szudzikPairingFunction(x, y))
                    str += when (point.tile) {
                        1 -> "X" // wall
                        2 -> "." // block
                        3 -> "-" // paddle
                        4 -> "o" // ball
                        else -> " " // empty
                    }
                }
                str += "\\\r\n"
            }
        }
        catch (e: Exception) {
            println(e.message)
        }
        println(str)
    }
}