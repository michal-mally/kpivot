package pl.helenium.kpivot

data class Dimension(private val fragments: List<*>) {
    constructor(vararg fragments: Any) : this(fragments.toList())

    fun allLevels() = generateSequence(this, Dimension::parent).toList()

    private fun parent() =
        fragments
            .takeIf { it.isNotEmpty() }
            ?.dropLast(1)
            ?.let(::Dimension)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")
}
