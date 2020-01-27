package pl.helenium.kpivot

import com.google.common.collect.Lists.cartesianProduct

fun <T, V> Collection<T>.pivot(
    valueExtractor: ValueExtractor<T, V>,
    dimensionsBuilder: (DimensionsBuilder<T>).(T) -> Unit
) = PivotTable(this, valueExtractor, dimensionsBuilder)

class PivotTable<T, V>(
    private val items: Collection<T>,
    private val valueExtractor: ValueExtractor<T, V>,
    private val dimensionsBuilder: (DimensionsBuilder<T>).(T) -> Unit
) {
    fun compute() =
        items
            .flatMap(this::cells)
            .groupBy(Cell<V>::dimensions)
            .mapValues { it.value.values() }
            .mapValues { valueExtractor.reduce(it.value) }

    private fun cells(item: T): List<Cell<V>> {
        val value = valueExtractor.extract(item)
        return DimensionsBuilder<T>()
            .apply { dimensionsBuilder(item) }
            .build()
            .allCombinations()
            .map { Cell(it, value) }
    }
}

class ValueExtractor<T, V>(
    val extract: (T) -> V,
    val reduce: (Collection<V>) -> V
)

class DimensionsBuilder<T> {
    private val dimensions = mutableListOf<Dimension>()

    fun dimension(vararg fragments: Any?) {
        this.dimensions.add(Dimension(fragments.toList()))
    }

    fun build() = Dimensions(dimensions)
}

data class Dimensions(private val dimensions: List<Dimension>) {
    fun allCombinations(): List<Dimensions> =
        cartesianProduct(dimensions.map(Dimension::allLevels))
            .map(::Dimensions)

    override fun toString() = dimensions.joinToString(separator = "x")
}

data class Dimension(private val fragments: List<*>) {
    fun allLevels() = generateSequence(this, Dimension::parent).toList()

    private fun parent() =
        fragments
            .takeIf(Collection<*>::isNotEmpty)
            ?.dropLast(1)
            ?.let(::Dimension)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")
}

private class Cell<V>(val dimensions: Dimensions, val value: V)

private fun <V> Collection<Cell<V>>.values() = map(Cell<V>::value)
