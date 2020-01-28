package pl.helenium.kpivot.operations

import pl.helenium.kpivot.ValueExtractor

fun <T> count() =
    ValueExtractor<T, Int>({ 1 }) {
        it.reduce(Int::plus)
    }

fun <T, V> sum(extract: (T) -> V, plus: BinaryOp<V>) =
    ValueExtractor(extract) {
        it.reduce(plus)
    }

fun <T, V> avg(extract: (T) -> V, plus: BinaryOp<V>, div: V.(Int) -> V) =
    ValueExtractor(extract) {
        it.reduce(plus).div(it.size)
    }

typealias BinaryOp<T> = (T, T) -> T
