class Intcode(private val intcode: Array<String>, private val phase:Int) {
    private var state = 0
    private var setPhase = true // Set 'phase¨ as 1st input for the Intcode

    private fun getValue(addr:Int, mode:Int): Int{
        val finalAddr = if (mode == 1) addr else intcode[addr].toInt()
        return intcode[finalAddr].toInt()
    }

    private fun writeValue(addr:Int, value:Int, mode:Int=0) {
        val finalAddr = if (mode == 1) addr else intcode[addr].toInt()
        intcode[finalAddr] = value.toString()
    }

    fun run(input: Int): Int {
        var output = -1
        loop@ while (state in 0 until intcode.count()) {
            val opcode = intcode[state]
            val op = if (opcode.length > 2) opcode.substring(opcode.length-2).toInt() else opcode.toInt()
            val mode1 = if (opcode.length > 2) opcode.substring(opcode.length-3, opcode.length-2).toInt() else 0
            val mode2 = if (opcode.length > 3) opcode.substring(opcode.length-4, opcode.length-3).toInt() else 0
            //val mode3 = if (opcode.length > 4) opcode.substring(opcode.length-5, opcode.length-4).toInt() else 0

            when (op) {
                1,2 -> { // op = 'add' / 'multiply'
                    val a = getValue(state+1, mode1)
                    val b = getValue(state+2, mode2)
                    val result = if (op == 1) a + b else a * b
                    writeValue(state+3, result)
                    state += 4
                }

                3 -> { // op = 'input'
                    val writeValue = if (setPhase) phase else input
                    writeValue(state + 1, writeValue)
                    setPhase = false
                    state += 2
                }

                4 -> { // op = 'output'
                    output = if (mode1 == 1) intcode[state + 1].toInt() else intcode[intcode[state + 1].toInt()].toInt()
                    state += 2
                    break@loop
                }

                5 -> { // op = 'jump-if-true'
                    state= if (getValue(state + 1, mode1) != 0) getValue(state + 2, mode2) else state + 3
                }

                6 -> { // op = 'jump-if-false'
                    state = if (getValue(state + 1, mode1) == 0) getValue(state + 2, mode2) else state + 3
                }

                7 -> { // op = 'less-than'
                    val a = getValue(state + 1, mode1)
                    val b = getValue(state + 2, mode2)
                    val result = if (a < b) 1 else 0
                    writeValue(state + 3, result)
                    state += 4
                }

                8 -> { // op = 'equals'
                    val a = getValue(state + 1, mode1)
                    val b = getValue(state + 2, mode2)
                    val result = if (a == b) 1 else 0
                    writeValue(state + 3, result)
                    state += 4
                }

                99 -> {
                    state = -1
                    output = if (output >= 0) output else input
                    break@loop
                }

                else -> {
                    println ("*** Invalid command = $op")
                    state = -1
                    return -1
                }
            }
        }
        return output
    }

    fun isFinished() : Boolean {
        return state < 0
    }
}