package pl.helenium.kpivot

import pl.helenium.kpivot.util.dropLast
import pl.helenium.kpivot.util.tail

data class Dimension(private val fragments: List<*>) : Comparable<Dimension> {
    constructor(vararg fragments: Any) : this(fragments.toList())

    // KF: List in Kotlin is immutable
    fun allLevels() =
        generateSequence(this, Dimension::parent)
            .toList()
            .reversed()

    private fun parent() =
        fragments
                // KF: getting rid of empty collections
            .takeIf { it.isNotEmpty() }
                // KF: using safe call operator
            ?.dropLast()
            ?.let(::Dimension)

    // KF: using named & default parameters
    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")

    override fun compareTo(other: Dimension): Int {
        // KF: Using tail recursion
            // KF: using local function 
        tailrec fun compareFragments(f1: List<*>, f2: List<*>): Int {
            if (f1 == f2) {
                return 0
            }

            // KF: using another local functionm 
            fun List<*>.firstToStringOrNull() = firstOrNull()?.toString()
            compareValues(
                // KF: using local returns
                f1.firstToStringOrNull() ?: return 1,
                f2.firstToStringOrNull() ?: return -1
            )
                .takeIf { it != 0 }
                    // KF: using return from inline function
                ?.let { return it }

            return compareFragments(f1.tail(), f2.tail())
        }

        return compareFragments(fragments, other.fragments)
    }

}
