fun main() {
    fun setupPages(input: List<String>): Pair<List<String>, List<List<Int>>> {
        var pageOrderingRules: MutableList<String> = mutableListOf()
        var pageUpdates: MutableList<List<Int>> = mutableListOf()

        var switch = false
        for (i in input) {
            if (i == "") {
                switch = true
                continue
            }

            if (switch) {
                pageUpdates.add(i.trim().split(",").map { it.toInt() })
            } else {
                pageOrderingRules.add(i)
            }
        }

        return Pair(pageOrderingRules, pageUpdates)
    }

    fun part1(input: List<String>): Int {
        var count = 0
        val (pageOrderingRules, pageUpdates) = setupPages(input)

        for (us in pageUpdates) {
            var ok = true
            for (i in 0..<us.size) {
                val u = us[i]

                for (rule in pageOrderingRules) {
                    val (start, end) = rule.split("|").map { it.toInt() }
                    if (u == start && us.contains(end)) {
                        // println("u: $u, start: $start, end: $end")
                        val slide = us.subList(0, i)
                        if (slide.contains(end)) {
                            ok = false
                        }
                    }
                }
            }

            if (ok) {
                count += us[us.size / 2]
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        val (pageOrderingRules, pageUpdates) = setupPages(input)

        for (us in pageUpdates) {
            var needsReordering = false
            for (i in 0..<us.size) {
                val u = us[i]
                for (rule in pageOrderingRules) {
                    val (start, end) = rule.split("|").map { it.toInt() }
                    if (u == start && us.contains(end)) {
                        val slide = us.subList(0, i)
                        if (slide.contains(end)) {
                            needsReordering = true
                            break
                        }
                    }
                }
                if (needsReordering) break
            }

            if (needsReordering) {
                val reordered = us.toMutableList()
                var changed: Boolean
                // keep reordering until no more changes are needed
                do {
                    changed = false
                    for (i in 0..<reordered.size) {
                        for (rule in pageOrderingRules) {
                            val (start, end) = rule.split("|").map { it.toInt() }
                            val startIndex = reordered.indexOf(start)
                            val endIndex = reordered.indexOf(end)
                            if (startIndex > endIndex && startIndex != -1 && endIndex != -1) {
                                val temp = reordered[startIndex]
                                reordered[startIndex] = reordered[endIndex]
                                reordered[endIndex] = temp
                                changed = true
                            }
                        }
                    }
                } while (changed)

                count += reordered[reordered.size / 2]
            }
        }
        return count
    }

    // Test if implementation meets criteria from the description, like:
    val testAnswerPart1 = part1(readInput("Day05_test"))
    check(testAnswerPart1 == 143)
    val testAnswerPart2 = part2(readInput("Day05_test"))
    check(testAnswerPart2 == 123)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
