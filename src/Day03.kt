

fun main() {
    fun part1(input: String): Int {
        val pattern = Regex("""mul\((\d+),(\d+)\)""")
        return pattern
            .findAll(input)
            .map { matchResult ->
                val (num1, num2) = matchResult.destructured
                num1.toInt() * num2.toInt()
            }.sum()
    }

    fun part2(input: String): Int {
        val mulPattern = Regex("""mul\((\d+),(\d+)\)""")
        val doPattern = Regex("""do\(\)""")
        val dontPattern = Regex("""don't\(\)""")

        // default enabled to support the start input
        var enabled = true
        var sum = 0

        val allMatches =
            (
                mulPattern.findAll(input) +
                    doPattern.findAll(input) +
                    dontPattern.findAll(input)
            ).sortedBy { it.range.first }

        for (match in allMatches) {
            when {
                match.value == "do()" -> enabled = true
                match.value == "don't()" -> enabled = false
                match.value.startsWith("mul") && enabled -> {
                    val (num1, num2) = match.destructured
                    sum += num1.toInt() * num2.toInt()
                }
            }
        }

        return sum
    }

    // Test if implementation meets criteria from the description, like:
    val testAnswerPart1 = part1(readInputString("Day03_test_part1"))
    check(testAnswerPart1 == 161)
    val testAnswerPart2 = part2(readInputString("Day03_test_part2"))
    check(testAnswerPart2 == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInputString("Day03")
    part1(input).println()
    part2(input).println()
}
