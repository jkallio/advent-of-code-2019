import java.io.File
import kotlin.math.max

/**
 * --- Day 6: Universal Orbit Map ---
 *
 * You've landed at the Universal Orbit Map facility on Mercury. Because navigation in space often involves transferring
 * between orbits, the orbit maps here are useful for finding efficient routes between, for example, you and Santa.
 * You download a map of the local orbits (your puzzle input).
 *
 * Except for the universal Center of Mass (COM), every object in space is in orbit around exactly one other object.
 * An orbit looks roughly like this:
 *
 *                     \
 *                      \
 *                       |
 *                       |
 *      AAA--> o         o <--BBB
 *                      |
 *                      |
 *                     /
 *                   /
 *
 * In this diagram, the object BBB is in orbit around AAA. The path that BBB takes around AAA (drawn with lines) is
 * only partly shown. In the map data, this orbital relationship is written AAA)BBB, which means "BBB is in orbit
 * around AAA".
 *
 * Before you use your map data to plot a course, you need to make sure it wasn't corrupted during the download.
 * To verify maps, the Universal Orbit Map facility uses orbit count checksums - the total number of direct orbits
 * (like the one shown above) and indirect orbits.
 *
 * Whenever A orbits B and B orbits C, then A indirectly orbits C. This chain can be any number of objects long: if A
 * orbits B, B orbits C, and C orbits D, then A indirectly orbits D.
 *
 * For example, suppose you have the following map:
 *      COM)B
 *      B)C
 *      C)D
 *      D)E
 *      E)F
 *      B)G
 *      G)H
 *      D)I
 *      E)J
 *      J)K
 *      K)L
 *
 * Visually, the above map of orbits looks like this:
 *
 *              G - H       J - K - L
 *             /           /
 *      COM - B - C - D - E - F
 *                    \
 *                     I
 *
 * In this visual representation, when two objects are connected by a line, the one on the right directly orbits the
 * one on the left.
 *
 * Here, we can count the total number of orbits as follows:
 *      D directly orbits C and indirectly orbits B and COM, a total of 3 orbits.
 *      L directly orbits K and indirectly orbits J, E, D, C, B, and COM, a total of 7 orbits.
 *      COM orbits nothing.
 *      The total number of direct and indirect orbits in this example is 42.
 *
 * What is the total number of direct and indirect orbits in your map data?
 *      -> Your puzzle answer was 122782.
 *
 *
 * --- Part Two ---
 * Now, you just need to figure out how many orbital transfers you (YOU) need to take to get to Santa (SAN).
 * You start at the object YOU are orbiting; your destination is the object SAN is orbiting. An orbital transfer lets
 * you move from any object to an object orbiting or orbited by that object.
 *
 * For example, suppose you have the following map:
 *      COM)B
 *      B)C
 *      C)D
 *      D)E
 *      E)F
 *      B)G
 *      G)H
 *      D)I
 *      E)J
 *      J)K
 *      K)L
 *      K)YOU
 *      I)SAN
 *
 * Visually, the above map of orbits looks like this:
 *                            YOU
 *                           /
 *          G - H       J - K - L
 *         /           /
 *  COM - B - C - D - E - F
 *                \
 *                 I - SAN
 *
 * In this example, YOU are in orbit around K, and SAN is in orbit around I. To move from K to I, a minimum of 4
 * orbital transfers are required:
 *          K to J
 *          J to E
 *          E to D
 *          D to I
 *
 * Afterward, the map of orbits looks like this:
 *
 *          G - H       J - K - L
 *         /           /
 *  COM - B - C - D - E - F
 *                \
 *                 I - SAN
 *                 \
 *                  YOU
 *
 * What is the minimum number of orbital transfers required to move from the object YOU are orbiting to the object SAN
 * is orbiting? (Between the objects they are orbiting - not between YOU and SAN.)
 *      -> Your puzzle answer was 271.
 */

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

fun pathToRoot(planet: Planet?): List<Planet> {
    val pathToRoot = mutableListOf<Planet>()
    planet?.let {
        var parent = it.parent
        while (parent != null) {
            pathToRoot.add(0, parent)
            parent = parent.parent
        }
    }
    return pathToRoot
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

    // Part-1
    var orbits = 0
    allPlanets.forEach {
        orbits += stepsToRoot(it.value)
    }
    println("Part-1: Total orbits = $orbits")

    // Part-2
    val pathToRootYOU = pathToRoot(allPlanets["YOU"])
    val pathToRootSAN = pathToRoot(allPlanets["SAN"])

    var steps = 0
    for (i in 0 until max(pathToRootSAN.count(), pathToRootYOU.count())) {
        val p1 = if (i < pathToRootYOU.count()) pathToRootYOU[i] else null
        val p2 = if (i < pathToRootSAN.count()) pathToRootSAN[i] else null
        if (p1 != p2) {
            steps += if (p1 != null) 1 else 0
            steps += if (p2 != null) 1 else 0
        }
    }
    print ("Part-2: Total steps from YOU to SAN = $steps")
}