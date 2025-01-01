fun main() {
    fun isAllAscDesc(input: List<Int>): Boolean {
        var diffs: MutableList<Int> = mutableListOf()
        val OkIncreaseNumbers: List<Int> = listOf(1,2,3)
        val OkDecreaseNumbers: List<Int> = listOf(-1,-2,-3)

        input.forEachIndexed(
            fun(
                index: Int,
                value: Int,
            ) {
                // skip first element, no diff to calculate
                if (index == 0) {
                    return
                }

                val prev: Int = input[index - 1]
                diffs.add(value - prev)
            },
        )

        return (
            diffs.all { OkIncreaseNumbers.contains(it) }
            || diffs.all { OkDecreaseNumbers.contains(it) }
        )
    }

    fun part1(input: List<String>): Int {
        var rv: Int = 0

        input.forEach(
            fun(line: String) {
                val report: List<Int> = line.split(" ").map { it.toInt() }
                val okReport: Boolean = isAllAscDesc(report)
                if (okReport) {
                    rv++
                }
            })

        return rv
    }

    fun part2(input: List<String>): Int {
        var rv: Int = 0

        input.forEach(
            fun(line: String) {
                val report: List<Int> = line.split(" ").map { it.toInt() }
                if (isAllAscDesc(report)) {
                    rv++
                } else {
                    var okEarly: Boolean = false
                    report.forEachIndexed(
                        fun(index: Int, _: Int) {
                            if (okEarly) {
                                return
                            }

                            var mutableReport: MutableList<Int> = report.toMutableList()
                            mutableReport.removeAt(index)
                            if (isAllAscDesc(mutableReport)) {
                                rv++
                                okEarly = true
                            }
                        }
                    )
                }
            })

        return rv
    }


    // Test if implementation meets criteria from the description, like:
    val testAnswerPart1 = part1(readInput("Day02_test"))
    check(testAnswerPart1 == 2)
    val testAnswerPart2 = part2(readInput("Day02_test"))
    check(testAnswerPart2 == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
