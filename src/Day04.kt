fun main() {
    fun isValidWord(
        grid: List<String>,
        startRow: Int,
        startCol: Int,
        dy: Int,
        dx: Int,
        word: String = "XMAS",
    ): Boolean {
        // out of bounds check
        if (startRow + dy * (word.length - 1) !in grid.indices ||
            startCol + dx * (word.length - 1) !in grid[0].indices
        ) {
            return false
        }
        return word.indices.all { i ->
            val row = startRow + dy * i
            val col = startCol + dx * i
            grid[row][col] == word[i]
        }
    }

    fun part1(input: List<String>): Int {
        val height: Int = input.size
        val width = input[0].length
        var count: Int = 0

        val directions =
            listOf(
                -1 to -1,
                -1 to 0,
                -1 to 1,
                0 to -1,
                0 to 1,
                1 to -1,
                1 to 0,
                1 to 1,
            )

        for (row in 0..<height) {
            for (col in 0..<width) {
                for ((dy, dx) in directions) {
                    if (isValidWord(input, row, col, dy, dx)) {
                        count++
                    }
                }
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        var count = 0

        for (row in 0..<height - 2) {
            for (col in 0..<width - 2) {
                if (input[row + 1][col + 1] != 'A') continue

                // check diagonals
                val topLeft = input[row][col]
                val topRight = input[row][col + 2]
                val bottomLeft = input[row + 2][col]
                val bottomRight = input[row + 2][col + 2]

                // check MAS or SAM
                if ((
                        (topLeft == 'M' && bottomRight == 'S') ||
                            (topLeft == 'S' && bottomRight == 'M')
                    ) &&
                    (
                        (topRight == 'M' && bottomLeft == 'S') ||
                            (topRight == 'S' && bottomLeft == 'M')
                    )
                ) {
                    count++
                }
            }
        }
        return count
    }

    // Test if implementation meets criteria from the description, like:
    val testAnswerPart1 = part1(readInput("Day04_test"))
    check(testAnswerPart1 == 18)
    val testAnswerPart2 = part2(readInput("Day04_test"))
    check(testAnswerPart2 == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
