class Intcode(private val intcode: Array<String>) {

    private fun getValue(addr:Int, mode:Int): Int{
        val finalAddr = if (mode == 1) addr else intcode[addr].toInt()
        return intcode[finalAddr].toInt()
    }

    private fun writeValue(addr:Int, value:Int, mode:Int=0) {
        val finalAddr = if (mode == 1) addr else intcode[addr].toInt()
        intcode[finalAddr] = value.toString()
    }

    fun run(inputs: Array<Int>): Int {
        var i = 0
        var inputIndex = 0
        var lastOutput = 0
        loop@ while (i < intcode.count()) {
            val opcode = intcode[i]
            val op = if (opcode.length > 2) opcode.substring(opcode.length-2).toInt() else opcode.toInt()
            val mode1 = if (opcode.length > 2) opcode.substring(opcode.length-3, opcode.length-2).toInt() else 0
            val mode2 = if (opcode.length > 3) opcode.substring(opcode.length-4, opcode.length-3).toInt() else 0
            val mode3 = if (opcode.length > 4) opcode.substring(opcode.length-5, opcode.length-4).toInt() else 0

            when (op) {
                1,2 -> { // op = 'add' / 'multiply'
                    val a = getValue(i+1, mode1)
                    val b = getValue(i+2, mode2)
                    val result = if (op == 1) a + b else a * b
                    writeValue(i+3, result)
                    i += 4
                }

                3 -> { // op = 'input'
                    var finalInput: Int? = null
                    if (inputIndex < inputs.count()) {
                        finalInput = inputs.elementAt(inputIndex)
                        inputIndex += 1
                    }
                    if (finalInput == null) {
                        print("Your input > ")
                        finalInput = readLine()!!.toIntOrNull()
                    }
                    finalInput?.let {
                        writeValue(i+1, it)
                        i += 2
                    }
                }

                4 -> { // op = 'output'
                    lastOutput = if (mode1 == 1) intcode[i+1].toInt() else intcode[intcode[i+1].toInt()].toInt()
                    i += 2
                }

                5 -> { // op = 'jump-if-true'
                    i = if (getValue(i+1, mode1) != 0) getValue(i+2, mode2) else i+3
                }

                6 -> { // op = 'jump-if-false'
                    i = if (getValue(i+1, mode1) == 0) getValue(i+2, mode2) else i+3
                }

                7 -> { // op = 'less-than'
                    val a = getValue(i+1, mode1)
                    val b = getValue(i+2, mode2)
                    val result = if (a < b) 1 else 0
                    writeValue(i+3, result)
                    i += 4
                }

                8 -> { // op = 'equals'
                    val a = getValue(i+1, mode1)
                    val b = getValue(i+2, mode2)
                    val result = if (a == b) 1 else 0
                    writeValue(i+3, result)
                    i += 4
                }

                99 -> {
                    break@loop
                }

                else -> {
                    println ("*** Invalid command = $op")
                    return -1
                }
            }
        }
        return lastOutput
    }
}