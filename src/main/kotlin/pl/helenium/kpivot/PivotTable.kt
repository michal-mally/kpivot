package pl.helenium.kpivot

// KF: extension function to turn every collection into Pivot Table
fun <T, V> Collection<T>.pivot(
    valueExtractor: ValueExtractor<T, V>,
    // KF: Lambda with a receiver
    dimensionsBuilder: Dimensions.(T) -> Unit
) = PivotTableBuilder(this, valueExtractor, dimensionsBuilder)

class PivotTableBuilder<T, V>(
    private val items: Collection<T>,
    private val valueExtractor: ValueExtractor<T, V>,
    private val dimensionsBuilder: Dimensions.(T) -> Unit
) {
    fun table() =
        // KF: function expression body
        items
            .flatMap(this::cells)
            .groupBy(Cell<V>::dimensions)
            .mapValues { it.value.values() }
            .mapValues { valueExtractor.reduce(it.value) }
                // KF: scope function `let` that allow to keep the chain
            .let(::PivotTable)

    private fun cells(item: T) =
        Dimensions()
                // KF: scope function `apply` - equivalent of `tap` in Groovy
                // KF: execution of lambda with receiver
            .apply { dimensionsBuilder(item) }
            .allCombinations()
            .map { Cell(it, valueExtractor.extract(item)) }
}

// KF: data class do you get toString(), equals() & hashCode() for free (+ copy)
data class PivotTable<V>(val values: Map<Dimensions, V>) {
    fun keysForDimension(index: Int) =
        values
            .keys
            .mapNotNull { it[index] }
            .toSortedSet()
}

class ValueExtractor<T, V>(
    val extract: (T) -> V,
    val reduce: (Collection<V>) -> V
)

private class Cell<V>(val dimensions: Dimensions, val value: V)

// KF: extension function on generic type
private fun <V> Collection<Cell<V>>.values() = map(Cell<V>::value)
