import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class Image (input: String, val width: Int, val height: Int) {
    private val layers: Array<Map<Int, Int>>

    init {
        val layerList = mutableListOf<MutableMap<Int,Int>>()
        var x = 0; var y = 0
        var layer = mutableMapOf<Int, Int>()
        input.forEach { ch ->
            layer[Position(x,y).hashCode()] = ch.toString().toInt()
            x += 1
            if (x >= width) {
                x = 0
                y += 1
                if (y >= height) {
                    y = 0
                    layerList.add(layer)
                    layer = mutableMapOf<Int, Int>()
                }
            }
        }
        layers = layerList.toTypedArray()
    }

    fun findLayerWithFewest(value: Int): Map<Int, Int>? {
        var fewest = Int.MAX_VALUE
        var retVal: Map<Int, Int>? = null
        layers.forEach { layer ->
            var count = 0
            layer.forEach {
                if (it.value == value) {
                    count += 1
                }
            }
            if (count < fewest) {
                fewest = count
                retVal = layer
            }
        }
        return retVal
    }

    fun numberOfValuesInLayer(layer: Map<Int, Int>, value: Int): Int {
        var count = 0
        layer.forEach {
            if (it.value == value) {
                count += 1
            }
        }
        return count
    }

    private fun mergeVisibleLayers(): Map<Int, Int>{
        val mergedLayer = mutableMapOf<Int, Int>()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val key = Position(x,y).hashCode()
                pixel@for (l in 0 until layers.count()) {
                    val digit: Int = layers[l][key] ?: 0
                    if (digit != 2) {
                        mergedLayer[key] = digit
                        break@pixel
                    }
                }
            }
        }
        return mergedLayer
    }

    fun saveAsJpg() {
        val flattened = mergeVisibleLayers()
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val rgb: Int = when (flattened[Position(x,y).hashCode()]) {
                    0 -> { Color.BLACK.rgb }
                    1 -> { Color.WHITE.rgb  }
                    else -> { Color.RED.rgb }
                }
                img.setRGB(x, y, rgb)
            }
        }
        try {
            val result = ImageIO.write(img, "jpg", File("output.jpg"))
            if (result) println("output.jpg Written successfully") else println("*** Failed to write image file")
        } catch (e: IOException) {
            println("*** Exception while ${e.message}")
        }
    }
}