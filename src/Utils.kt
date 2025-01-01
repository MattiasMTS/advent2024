import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.text.padStart

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = readInputString(name).lines()

/**
 * Reads the whole content of the given input file.
 */
fun readInputString(name: String): String = Path("src/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
