package pl.helenium.kpivot

import pl.helenium.kpivot.util.dropLast
import pl.helenium.kpivot.util.tail

data class Dimension(private val fragments: List<*>) : Comparable<Dimension> {
    constructor(vararg fragments: Any) : this(fragments.toList())

    fun allLevels() =
        generateSequence(this, Dimension::parent)
            .toList()
            .reversed()

    private fun parent() =
        fragments
            .takeIf { it.isNotEmpty() }
            ?.dropLast()
            ?.let(::Dimension)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")

    override fun compareTo(other: Dimension): Int {
        tailrec fun compareFragments(f1: List<*>, f2: List<*>): Int {
            if (f1 == f2) {
                return 0
            }

            fun List<*>.firstToStringOrNull() = firstOrNull()?.toString()
            compareValues(
                f1.firstToStringOrNull() ?: return 1,
                f2.firstToStringOrNull() ?: return -1
            )
                .takeIf { it != 0 }
                ?.let { return it }

            return compareFragments(f1.tail(), f2.tail())
        }

        return compareFragments(fragments, other.fragments)
    }

}
