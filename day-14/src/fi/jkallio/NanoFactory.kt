package fi.jkallio

import kotlin.math.ceil
import kotlin.math.max

class NanoFactory(private val reactions: List<Reaction>) {
    val supply = mutableMapOf<String, Long>()
    var oreConsumption = 0

    private fun getReaction(elementName: String): Reaction? {
        reactions.find { it.result.first == elementName }?.let { reaction ->
            return reaction
        }
        return null
    }

    private fun storeLeftOver(element: Element) {
        if (element.second > 0) {
            supply[element.first] = supply.getOrDefault(element.first, 0) + element.second
            debugPrint("${element.second} ${element.first} left over")
        }
    }

    private fun getLeftOvers(element: Element): Long{
        val value = supply.getOrDefault(element.first, 0)
        if (value > 0) {
            debugPrint("Supply consumed; ${element.first} = $value")
            supply[element.first] = 0
        }
        return value
    }

    fun create(element: Element) {
        getReaction(element.first)?.let { reaction ->
            debugPrint("$element => $reaction {", 1)
            val reactionResult = reaction.result.second
            val amountToCreate = element.second - getLeftOvers(element)
            val multiplier = ceil(max(amountToCreate.toDouble(), reactionResult.toDouble()) / reactionResult.toDouble()).toLong()
            debugPrint("${element.first}: Req=${element.second}; multipliers=$multiplier")

            // Store created & leftover chemicals
            storeLeftOver(Element(element.first, (multiplier * reaction.result.second) - amountToCreate))

            // Call recursively for each input reaction
            reaction.inputs.forEach { input ->
                create(Element(input.first, multiplier * input.second))
            }
            debugPrint("}", -1)
        }
    }

    private var tabCount = 0 // Tab (\t) counter for debug print formatting
    private fun debugPrint(any: Any, tabs: Int = 0) {
        tabCount -= if (tabs < 0) 1 else 0
        var t = ""
        for (i in 0 until tabCount) { t += "\t"}
        println("$t${any.toString()}")
        tabCount += if (tabs > 0) 1 else 0
    }
}