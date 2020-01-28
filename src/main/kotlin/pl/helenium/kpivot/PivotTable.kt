package pl.helenium.kpivot

import com.google.common.collect.Lists.cartesianProduct

fun <T, V> Collection<T>.pivot(
    valueExtractor: ValueExtractor<T, V>,
    dimensionsBuilder: Dimensions.(T) -> Unit
) = PivotTableBuilder(this, valueExtractor, dimensionsBuilder)

class PivotTableBuilder<T, V>(
    private val items: Collection<T>,
    private val valueExtractor: ValueExtractor<T, V>,
    private val dimensionsBuilder: Dimensions.(T) -> Unit
) {
    fun table() =
        items
            .flatMap(this::cells)
            .groupBy(Cell<V>::dimensions)
            .mapValues { it.value.values() }
            .mapValues { valueExtractor.reduce(it.value) }
            .let(::PivotTable)

    private fun cells(item: T) =
        Dimensions()
            .apply { dimensionsBuilder(item) }
            .allCombinations()
            .map { Cell(it, valueExtractor.extract(item)) }
}

data class PivotTable<V>(val values: Map<Dimensions, V>)

class ValueExtractor<T, V>(
    val extract: (T) -> V,
    val reduce: (Collection<V>) -> V
)

data class Dimensions(private val dimensions: MutableList<Dimension> = mutableListOf()) {
    fun dimension(vararg fragments: Any?) {
        dimensions += Dimension(fragments.toList())
    }

    fun allCombinations(): List<Dimensions> =
        cartesianProduct(dimensions.map(Dimension::allLevels))
            .map(::Dimensions)

    override fun toString() = dimensions.joinToString(separator = "x")
}

data class Dimension(private val fragments: List<*>) {
    fun allLevels() = generateSequence(this, Dimension::parent).toList()

    private fun parent() =
        fragments
            .takeIf { it.isNotEmpty() }
            ?.dropLast(1)
            ?.let(::Dimension)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")
}

private class Cell<V>(val dimensions: Dimensions, val value: V)

private fun <V> Collection<Cell<V>>.values() = map(Cell<V>::value)
