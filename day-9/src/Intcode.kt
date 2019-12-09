class Intcode(source: Array<String>) {
    private var pos: Int = 0
    private var base = 0
    private val intcode = arrayOfNulls<String>(10000)

    init {
        for (i in intcode.indices) {
            intcode[i] = if (i < source.count()) source[i] else "0"
        }
    }

    private fun getAddr(addr:Int, mode:Int): Int {
        return when (mode) {
            0 -> intcode[addr]!!.toInt()            // Position mode
            1 -> addr                               // Direct mode
            2 ->  base + intcode[addr]!!.toInt()    // Relative mode
            else -> -1
        }
    }

    private fun getLongValue(addr:Int, mode:Int) : Long {
        val finalAddr = getAddr(addr, mode)
        return intcode[finalAddr]!!.toLong()
    }

    private fun getIntValue(addr:Int, mode:Int): Int{
        val finalAddr = getAddr(addr, mode)
        return intcode[finalAddr]!!.toInt()
    }

    private fun writeLongValue(addr:Int, value:Long, mode:Int) {
        val finalAddr = getAddr(addr, mode)
        intcode[finalAddr] = value.toString()
    }
    private fun writeIntValue(addr:Int, value:Int, mode:Int) {
        val finalAddr = getAddr(addr, mode)
        intcode[finalAddr] = value.toString()
    }

    fun run(input: Long? = null): Long {
        var output: Long = -1
        loop@ while (pos in 0 until intcode.count()) {
            val opcode = intcode[pos]!!
            val op = if (opcode.length > 2) opcode.substring(opcode.length-2).toInt() else opcode.toInt()
            val mode1 = if (opcode.length > 2) opcode.substring(opcode.length-3, opcode.length-2).toInt() else 0
            val mode2 = if (opcode.length > 3) opcode.substring(opcode.length-4, opcode.length-3).toInt() else 0
            val mode3 = if (opcode.length > 4) opcode.substring(opcode.length-5, opcode.length-4).toInt() else 0

            when (op) {
                1,2 -> { // op = 'add' / 'multiply'
                    val a = getLongValue(pos+1, mode1)
                    val b = getLongValue(pos+2, mode2)
                    val result = if (op == 1) a + b else a * b
                    writeLongValue(pos+3, result, mode3)
                    pos += 4
                }

                3 -> { // op = 'input'
                    if (input == null) {
                        println ("*** Invalid input (null)")
                        break@loop
                    }
                    writeLongValue(pos + 1, input, mode1)
                    pos += 2
                }

                4 -> { // op = 'output'
                    output = getLongValue(pos + 1, mode1)
                    println("Output = $output")
                    pos += 2
                }

                5 -> { // op = 'jump-if-true'
                    pos = if (getIntValue(pos + 1, mode1) != 0) getIntValue(pos + 2, mode2) else pos + 3
                }

                6 -> { // op = 'jump-if-false'
                    pos = if (getIntValue(pos + 1, mode1) == 0) getIntValue(pos + 2, mode2) else pos + 3
                }

                7 -> { // op = 'less-than'
                    val a = getLongValue(pos + 1, mode1)
                    val b = getLongValue(pos + 2, mode2)
                    val result = if (a < b) 1 else 0
                    writeIntValue(pos + 3, result, mode3)
                    pos += 4
                }

                8 -> { // op = 'equals'
                    val a = getIntValue(pos + 1, mode1)
                    val b = getIntValue(pos + 2, mode2)
                    val result = if (a == b) 1 else 0
                    writeIntValue(pos + 3, result, mode3)
                    pos += 4
                }

                9 -> {  // op = 'adjust base'
                    base += getIntValue(pos+1, mode1)
                    pos += 2
                }

                99 -> {
                    pos = -1
                    output = if (output >= 0) output else input?:-1
                    break@loop
                }

                else -> {
                    println ("*** Invalid command = $op")
                    pos = -1
                    output = -1
                    break@loop
                }
            }
        }
        return output
    }
}