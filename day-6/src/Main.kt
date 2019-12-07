import java.io.File

val allPlanets = mutableMapOf<String, Planet>()
var tabCount = 0

fun debugPrintPlanet(planet: Planet) {
    var tabs = ""
    for (i in 0 until tabCount) { tabs += "-" }
    println("${tabs}${planet.id} (${planet.orbitingPlanets.count()})")
    tabCount += 1
    planet.orbitingPlanets.forEach {
        debugPrintPlanet(it)
    }
    tabCount -= 1
}

fun stepsToRoot(planet: Planet) : Int {
    planet.parent?.let {
        return 1 + stepsToRoot(it)
    }
    return 0
}

fun main(args: Array<String>) {
    if (args.count() != 1) {
        println("*** Missing input file ***")
        return
    }
    val input = mutableListOf<String>()
    File(args[0]).forEachLine { input.add(it) }

    input.forEach {
        val params = it.split(')')
        val planet1 = allPlanets.getOrPut(params[0]) { Planet(params[0]) }
        val planet2 = allPlanets.getOrPut(params[1]) { Planet(params[1]) }

        // Set cross-references
        planet2.parent = planet1
        planet1.orbitingPlanets.add(planet2)
    }

    var orbits = 0
    allPlanets.forEach {
        orbits += stepsToRoot(it.value)
    }
    println("Total orbits = $orbits")
}