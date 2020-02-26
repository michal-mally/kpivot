package pl.helenium.kpivot

import com.google.common.collect.Lists.cartesianProduct

data class Dimensions(private val dimensions: MutableList<Dimension> = mutableListOf()) {
    // KF: Using varargs
    constructor(vararg dimensions: Dimension) : this(dimensions.toMutableList())

    fun dimension(vararg fragments: Any?) {
        // KF: using overloaded operator function
        dimensions += Dimension(fragments.toList())
    }

    fun allCombinations() =
        cartesianProduct(dimensions.map(Dimension::allLevels))
            .map(::Dimensions)

    // KF: overriding [] operator
    operator fun get(index: Int) = dimensions.getOrNull(index)

    override fun toString() = dimensions.joinToString(separator = "x")
}
