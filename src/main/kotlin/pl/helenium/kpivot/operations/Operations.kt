package pl.helenium.kpivot.operations

import pl.helenium.kpivot.ValueExtractor

fun <T> count() =
    // KF: function passed as last arg can be passed outside of parenthesis
    ValueExtractor<T, Int>({ 1 }) {
        it.reduce(Int::plus)
    }

fun <T, V> sum(extract: (T) -> V, plus: BinaryOp<V>) =
    ValueExtractor(extract) {
        it.reduce(plus)
    }

fun <T, V> avg(extract: (T) -> V, plus: BinaryOp<V>, div: V.(Int) -> V) =
    ValueExtractor(extract) {
        // KF: calling lambda w/ receiver as a regular function of V
        it.reduce(plus).div(it.size)
    }

// KF: creating a named alias
typealias BinaryOp<T> = (T, T) -> T
