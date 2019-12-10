package fi.jkallio
import java.io.File

/**
 * test_input1.txt: Best is 5,8 with 33 other asteroids detected:
 * test_input2.txt: Best is 1,2 with 35 other asteroids detected
 * test_input3.txt: Best is 6,3 with 41 other asteroids detected
 * test_input4.txt: Best is 11,13 with 210 other asteroids detected
 * input.txt: Your input
 */

fun main(args: Array<String>) {
    val inputFile = args[1]
    println("Input file: $inputFile")
    SpaceMap(File(inputFile).readLines())
}