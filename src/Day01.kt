import kotlin.math.abs

fun main() {
    fun getLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val list1: MutableList<Int> = mutableListOf()
        val list2: MutableList<Int> = mutableListOf()

        for (i in input) {
            val temp: List<String> = i.split("   ")
            try {
                val tt1 = temp[0].toInt()
                val tt2 = temp[1].toInt()
                list1.add(tt1)
                list2.add(tt2)
            } catch (e: NumberFormatException) {
                println("Error: $e")
            }
        }

        return Pair(list1, list2)
    }

    fun part1(input: List<String>): Int {
        // sorted lists ascendingly
        val (list1, list2) = getLists(input)

        val list1Sorted = list1.sorted()
        val list2Sorted = list2.sorted()

        var total: Int = 0

        for (i in 0..<list1Sorted.size) {
            val t1 = list1Sorted[i]
            val t2 = list2Sorted[i]

            total += abs(t2 - t1)
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val (list1, list2) = getLists(input)
        var total: Int = 0

        for (i in list1) {
            var count: Int = 0
            for (j in list2) {
                if (i == j) {
                    count++
                }
            }
            total += count * i
        }

        return total
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01_test")) == 11)
    check(part2(readInput("Day01_test")) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
