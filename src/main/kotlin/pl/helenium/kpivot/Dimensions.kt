package pl.helenium.kpivot

import com.google.common.collect.Lists.cartesianProduct

data class Dimensions(private val dimensions: MutableList<Dimension> = mutableListOf()) {
    constructor(vararg dimensions: Dimension) : this(dimensions.toMutableList())

    fun dimension(vararg fragments: Any?) {
        dimensions += Dimension(fragments.toList())
    }

    fun allCombinations() =
        cartesianProduct(dimensions.map(Dimension::allLevels))
            .map(::Dimensions)

    override fun toString() = dimensions.joinToString(separator = "x")
}
