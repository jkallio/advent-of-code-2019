package fi.jkallio

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs

enum class Tile {
    UNKNOWN,
    START,
    EMPTY,
    WALL,
    OXYGEN_SYSTEM
}

fun MutableMap<Int, Point>.findTopLeft(): Point {
    val topLeft = Point(0,0)
    values.forEach { p ->
        topLeft.x = if (p.x < topLeft.x) p.x else topLeft.x
        topLeft.y = if (p.y > topLeft.y) p.y else topLeft.y
    }
    return topLeft
}

fun MutableMap<Int, Point>.findBottomRight(): Point {
    val bottomRight = Point(0,0)
    values.forEach { p ->
        bottomRight.x = if (p.x > bottomRight.x) p.x else bottomRight.x
        bottomRight.y = if (p.y < bottomRight.y) p.y else bottomRight.y
    }
    return bottomRight
}

fun MutableMap<Int, Point>.saveAsTxt(filename: String) {
    val topLeft = findTopLeft()
    val botRight = findBottomRight()
    var str = ""
    for (y in topLeft.y downTo botRight.y) {
        for (x in topLeft.x..botRight.x) {
            val point = getOrDefault(Point.szudzikPairingFunction(x,y), Point(x, y, Tile.UNKNOWN))
            str += when(point.tile) {
                Tile.START -> "S"
                Tile.EMPTY -> "."
                Tile.WALL -> "#"
                Tile.OXYGEN_SYSTEM -> "O"
                Tile.UNKNOWN -> "\\"
            }
        }
        str += "\r\n"
    }
    File(filename).writeText(str)
}


fun MutableMap<Int, Point>.saveAsJpg(filename: String): Boolean {
    val topLeft = findTopLeft()
    val botRight = findBottomRight()
    val width = abs(botRight.x - topLeft.x) + 1
    val height = abs(topLeft.y - botRight.y) + 1

    var imgX = 0
    var imgY = 0
    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for (y in topLeft.y downTo botRight.y) {
        for (x in topLeft.x until botRight.x) {
            val point = getOrDefault(Point.szudzikPairingFunction(x,y), Point(x, y, Tile.UNKNOWN))
            val rgb: Int = when(point.tile) {
                Tile.START -> Color.BLUE.rgb
                Tile.EMPTY -> Color.WHITE.rgb
                Tile.WALL -> Color.BLACK.rgb
                Tile.OXYGEN_SYSTEM -> Color.RED.rgb
                Tile.UNKNOWN -> Color.GRAY.rgb
            }
            img.setRGB(imgX, imgY, rgb)
            imgX += 1
        }
        imgX = 0
        imgY+= 1
    }
    return ImageIO.write(img, "jpg", File(filename))
}
