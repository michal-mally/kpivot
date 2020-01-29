package pl.helenium.kpivot

import pl.helenium.kpivot.util.tail

data class Dimension(private val fragments: List<*>) : Comparable<Dimension> {
    constructor(vararg fragments: Any) : this(fragments.toList())

    fun allLevels() = generateSequence(this, Dimension::parent).toList()

    private fun parent() =
        fragments
            .takeIf { it.isNotEmpty() }
            ?.tail()
            ?.let(::Dimension)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")

    override fun compareTo(other: Dimension): Int {
        tailrec fun compareFragments(f1: List<*>, f2: List<*>): Int {
            if (f1.isEmpty() && f2.isEmpty()) {
                return 0
            }

            listOf(
                f1.takeIf { it.isNotEmpty() } ?: return 1,
                f2.takeIf { it.isNotEmpty() } ?: return -1
            )
                .map { it.first() }
                .map { it.toString() }
                .let { it[0].compareTo(it[1]) }
                .takeIf { it != 0 }
                ?.let { return it }

            return compareFragments(f1.tail(), f2.tail())
        }

        return compareFragments(fragments, other.fragments)
    }

}
