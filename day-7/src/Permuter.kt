class Permuter(numbers: Array<Int>) {
    val permutations = mutableListOf<Array<Int>>()

    init {
        permute(numbers, 0, numbers.count()-1)
    }

    private fun swap(numbers: Array<Int>, a: Int, b: Int) {
        numbers[a] = numbers[b].also { numbers[b] = numbers[a] }
    }

    private fun permute(numbers: Array<Int>, start: Int, end: Int) {
        if (start == end) {
            permutations.add(numbers.copyOf())
        }
        else {
            for (i in start..end) {
                swap(numbers, start, i)
                permute(numbers, start+1, end)
                swap(numbers, i, start)
            }
        }
    }
}