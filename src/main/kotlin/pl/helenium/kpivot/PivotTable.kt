package pl.helenium.kpivot

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

data class PivotTable<V>(val values: Map<Dimensions, V>) {
    fun keysForDimension(index: Int) = values
        .keys
        .mapNotNull { it[index] }
        .distinct()
}

class ValueExtractor<T, V>(
    val extract: (T) -> V,
    val reduce: (Collection<V>) -> V
)

private class Cell<V>(val dimensions: Dimensions, val value: V)

private fun <V> Collection<Cell<V>>.values() = map(Cell<V>::value)
