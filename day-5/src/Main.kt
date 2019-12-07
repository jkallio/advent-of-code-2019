import java.io.File

/**
 * --- Day 5: Sunny with a Chance of Asteroids ---
 * You're starting to sweat as the ship makes its way toward Mercury. The Elves suggest that you get the air conditioner
 * working by upgrading your ship computer to support the Thermal Environment Supervision Terminal.
 *
 * The Thermal Environment Supervision Terminal (TEST) starts by running a diagnostic program (your puzzle input). The
 * TEST diagnostic program will run on your existing Intcode computer after a few modifications:
 *
 * First, you'll need to add two new instructions:
 *      - Opcode 3 takes a single integer as input and saves it to the position given by its only parameter.
 *          For example,the instruction 3,50 would take an input value and store it at address 50.
 *      - Opcode 4 outputs the value of its only parameter. For example, the instruction 4,50 would output the
 *          value at address 50.
 *
 * Programs that use these instructions will come with documentation that explains what should be connected to the input
 * and output. The program 3,0,4,0,99 outputs whatever it gets as input, then halts.
 *
 * Second, you'll need to add support for parameter modes:
 *
 * Each parameter of an instruction is handled based on its parameter mode. Right now, your ship computer already
 * understands parameter mode 0, position mode, which causes the parameter to be interpreted as a position - if the
 * parameter is 50, its value is the value stored at address 50 in memory. Until now, all parameters have been in
 * position mode.
 *
 * Now, your ship computer will also need to handle parameters in mode 1, immediate mode. In immediate mode, a parameter
 * is interpreted as a value - if the parameter is 50, its value is simply 50.
 *
 * Parameter modes are stored in the same value as the instruction's opcode. The opcode is a two-digit number based only
 * on the ones and tens digit of the value, that is, the opcode is the rightmost two digits of the first value in an
 * instruction. Parameter modes are single digits, one per parameter, read right-to-left from the opcode: the first
 * parameter's mode is in the hundreds digit, the second parameter's mode is in the thousands digit, the third
 * parameter's mode is in the ten-thousands digit, and so on. Any missing modes are 0.
 *
 * For example, consider the program 1002,4,3,4,33.
 * The first instruction, 1002,4,3,4, is a multiply instruction - the rightmost two digits of the first value, 02,
 * indicate opcode 2, multiplication. Then, going right to left, the parameter modes are 0 (hundreds digit),
 * 1 (thousands digit), and 0 (ten-thousands digit, not present and therefore zero):
 *
 *      ABCDE
 *      -1002
 *
 *      DE - two-digit opcode,      02 == opcode 2
 *      C - mode of 1st parameter,  0 == position mode
 *      B - mode of 2nd parameter,  1 == immediate mode
 *      A - mode of 3rd parameter,  0 == position mode, omitted due to being a leading zero
 *
 * This instruction multiplies its first two parameters. The first parameter, 4 in position mode, works like it did
 * before - its value is the value stored at address 4 (33). The second parameter, 3 in immediate mode, simply has
 * value 3. The result of this operation, 33 * 3 = 99, is written according to the third parameter, 4 in position mode,
 * which also works like it did before - 99 is written to address 4.
 *
 * Parameters that an instruction writes to will never be in immediate mode.
 * Finally, some notes:
 *      - It is important to remember that the instruction pointer should increase by the number of values in the
 *          instruction after the instruction finishes. Because of the new instructions, this amount is no longer always 4.
 *      - Integers can be negative: 1101,100,-1,4,0 is a valid program (find 100 + -1, store the result in position 4).
 *
 * The TEST diagnostic program will start by requesting from the user the ID of the system to test by running an input
 * instruction - provide it 1, the ID for the ship's air conditioner unit.
 *
 * It will then perform a series of diagnostic tests confirming that various parts of the Intcode computer, like
 * parameter modes, function correctly. For each test, it will run an output instruction indicating how far the result
 * of the test was from the expected value, where 0 means the test was successful. Non-zero outputs mean that a function
 * is not working correctly; check the instructions that were run before the output instruction to see which one failed.
 *
 * Finally, the program will output a diagnostic code and immediately halt. This final output isn't an error; an output
 * followed immediately by a halt means the program finished. If all outputs were zero except the diagnostic code, the
 * diagnostic program ran successfully.
 *
 * After providing 1 to the only input instruction and passing all the tests, what diagnostic code does the program
 * produce?
*/

fun runIntCode(intcode: Array<String>) : Int {
    var i = 0
    var lastOutput = 0
    loop@ while (i < intcode.count()) {
        val opcode = intcode[i]
        val op = if (opcode.length > 2) opcode.substring(opcode.length-2).toInt() else opcode.toInt()
        val mode1 = if (opcode.length > 2) opcode.substring(opcode.length-3, opcode.length-2).toInt() else 0
        val mode2 = if (opcode.length > 3) opcode.substring(opcode.length-4, opcode.length-3).toInt() else 0
        val mode3 = if (opcode.length > 4) opcode.substring(opcode.length-5, opcode.length-4).toInt() else 0

        when (op) {
            1,2 -> { // op = 'add' / 'multiply'
                val a = if (mode1 == 1) intcode[i+1].toInt() else intcode[intcode[i+1].toInt()].toInt()
                val b = if (mode2 == 1) intcode[i+2].toInt() else intcode[intcode[i+2].toInt()].toInt()
                assert(mode3 == 0) // Writing operations  must always be 'position mode'
                val storePos = intcode[i+3].toInt()
                val result = if (op == 1) a + b else a * b
                intcode[storePos] = result.toString()
                i += 4
            }

            3 -> { // op = 'input'
                assert(mode1 == 0 && mode2 == 0 && mode3 == 0)
                print("Your input > ")
                val strInput = readLine()!!
                //val strInput = "1" // Always input '1'

                val input = strInput.toIntOrNull()
                if (input == null) {
                    println ("*** Invalid user input ***")
                    return -1

                }

                val storePos = intcode[i + 1].toInt()
                intcode[storePos] = input.toString()
                i += 2
            }

            4 -> { // op = 'output'
                lastOutput = if (mode1 == 1) intcode[i+1].toInt() else intcode[intcode[i+1].toInt()].toInt()
                println("Output: $lastOutput")
                i += 2
            }

            99 -> {
                break@loop
            }

            else -> {
                println ("*** Critical failure ***")
                return -1
            }
        }
    }
    return lastOutput
}

fun main(args: Array<String>) {

    if (args.count() != 1)  {
        print("Missing argument: <filename>")
        return
    }

    // Read the input file
    var input = ""
    File(args[0]).forEachLine { input += it }

    // Split comma separated integers into a list
    val intcode = mutableListOf<String>()
    input.split(',').forEach { intcode.add(it) }
    val runResult = runIntCode(intcode.toTypedArray())
    println("Intcode result = $runResult")

}