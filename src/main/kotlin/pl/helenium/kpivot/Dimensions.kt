package pl.helenium.kpivot

import com.google.common.collect.Lists

data class Dimensions(private val dimensions: MutableList<Dimension> = mutableListOf()) {
    fun dimension(vararg fragments: Any?) {
        dimensions += Dimension(fragments.toList())
    }

    fun allCombinations(): List<Dimensions> =
        Lists.cartesianProduct(dimensions.map(Dimension::allLevels))
            .map(::Dimensions)

    override fun toString() = dimensions.joinToString(separator = "x")
}
