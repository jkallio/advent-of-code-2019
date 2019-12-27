package fi.jkallio

/**
 * --- Day 15: Oxygen System ---
 * Out here in deep space, many things can go wrong. Fortunately, many of those things have indicator lights.
 * Unfortunately, one of those lights is lit: the oxygen system for part of the ship has failed!
 *
 * According to the readouts, the oxygen system must have failed days ago after a rupture in oxygen tank two; that
 * section of the ship was automatically sealed once oxygen levels went dangerously low. A single remotely-operated
 * repair droid is your only option for fixing the oxygen system.
 *
 * The Elves' care package included an Intcode program (your puzzle input) that you can use to remotely control the
 * repair droid. By running that program, you can direct the repair droid to the oxygen system and fix the problem.
 *
 * The remote control program executes the following steps in a loop forever:
 *      Accept a movement command via an input instruction.
 *      Send the movement command to the repair droid.
 *      Wait for the repair droid to finish the movement operation.
 *      Report on the status of the repair droid via an output instruction.
 *
 * Only four movement commands are understood: north (1), south (2), west (3), and east (4). Any other command is
 * invalid. The movements differ in direction, but not in distance: in a long enough east-west hallway, a series of
 * commands like 4,4,4,4,3,3,3,3 would leave the repair droid back where it started.
 *
 * The repair droid can reply with any of the following status codes:
 *      0: The repair droid hit a wall. Its position has not changed.
 *      1: The repair droid has moved one step in the requested direction.
 *      2: The repair droid has moved one step in the requested direction; And the oxygen system was found.
 *
 * You don't know anything about the area around the repair droid, but you can figure it out by watching the status codes.
 *
 * What is the fewest number of movement commands required to move the repair droid from its starting position to the
 * location of the oxygen system?
 *      --> Your puzzle answer was 214.
 *
 *
 * --- Part Two ---
 * You quickly repair the oxygen system; oxygen gradually fills the area.
 *
 * Oxygen starts in the location containing the repaired oxygen system. It takes one minute for oxygen to spread to all
 * open locations that are adjacent to a location that already contains oxygen. Diagonal locations are not adjacent.
 *
 * In the example above, suppose you've used the droid to explore the area fully and have the following map (where
 * locations that currently contain oxygen are marked O):
 *       ##
 *      #..##
 *      #.#..#
 *      #.O.#
 *       ###
 *
 * Initially, the only location which contains oxygen is the location of the repaired oxygen system. However, after one
 * minute, the oxygen spreads to all open (.) locations that are adjacent to a location containing oxygen:
 *       ##
 *      #..##
 *      #.#..#
 *      #OOO#
 *       ###
 *
 * After a total of two minutes, the map looks like this:
 *       ##
 *      #..##
 *      #O#O.#
 *      #OOO#
 *       ###
 *
 * After a total of three minutes:
 *       ##
 *      #O.##
 *      #O#OO#
 *      #OOO#
 *       ###
 *
 * And finally, the whole region is full of oxygen after a total of four minutes:
 *       ##
 *      #OO##
 *      #O#OO#
 *      #OOO#
 *       ###
 *
 * So, in this example, all locations contain oxygen after 4 minutes.
 * Use the repair droid to get a complete map of the area. How many minutes will it take to fill with oxygen?
 *      --> Your puzzle answer was 344
 */

import java.io.File
import javax.swing.Box

fun mapShip(initDroid: Droid, reverseMode: Boolean = false): Droid? {
    var winningDroid: Droid? = null
    var idPool = 1
    val droidList = mutableListOf(
        initDroid.cloneWithDirection(idPool++, Droid.Direction.NORTH),
        initDroid.cloneWithDirection(idPool++, Droid.Direction.SOUTH),
        initDroid.cloneWithDirection(idPool++, Droid.Direction.EAST),
        initDroid.cloneWithDirection(idPool++, Droid.Direction.WEST))

    while (droidList.isNotEmpty()) {
        val deadList = mutableListOf<Droid>()
        val cloneList = mutableListOf<Droid>()
        for (i in droidList.indices) {
            val droid = droidList[i]
            droid.move()
            when {
                droid.isDead -> {
                    deadList.add(droid)
                }
                droid.foundOxygenSystem -> {
                    markRoute(droid)
                    winningDroid = droid
                    deadList.add(droid)
                }
                else -> {
                    when (droid.direction) {
                        Droid.Direction.NORTH, Droid.Direction.SOUTH -> {
                            cloneList.add(droid.cloneWithDirection(idPool++, Droid.Direction.EAST))
                            cloneList.add(droid.cloneWithDirection(idPool++, Droid.Direction.WEST))
                        }
                        Droid.Direction.EAST, Droid.Direction.WEST -> {
                            cloneList.add(droid.cloneWithDirection(idPool++, Droid.Direction.NORTH))
                            cloneList.add(droid.cloneWithDirection(idPool++, Droid.Direction.SOUTH))
                        }
                    }
                }
            }
            if (reverseMode) {
                winningDroid = droid // In reverse mode last surviving droid will be returned
            }
        }
        droidList.addAll(cloneList)
        droidList.removeAll(deadList)
    }
    return winningDroid
}

fun markRoute(droid: Droid) {
    droid.route.forEach {
        droid.shipMap.get(it.hashCode())?.let { point ->
            if (point.tile == Tile.EMPTY) {
                point.tile = Tile.WINNING_ROUTE
            }
        }
    }
}

fun main(args: Array<String>) {
    val intcodeInput = File(args[0]).readLines()[0].split(",")
    val shipMap = mutableMapOf<Int, Point>()
    var winningDroid = mapShip(Droid(1, Intcode(intcodeInput), shipMap, Droid.Direction.NORTH))
    println("----------------------------------")
    println("Oxygen System found: $winningDroid")

    shipMap.saveAsTxt("output.txt")
    shipMap.saveAsJpg("output.png")

    winningDroid?.let { droid ->
        shipMap.clear()
        droid.route.clear()
        winningDroid = mapShip(droid, true)
        println("----------------------------------")
        println("Steps to fill ship: ${winningDroid!!.route.count()-1}")
    }
}