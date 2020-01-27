package pl.helenium.kpivot

import java.math.BigDecimal

interface Operations<T> {
    fun plus(a: T, b: T): T
}

class BigDecimalOperations : Operations<BigDecimal> {
    override fun plus(a: BigDecimal, b: BigDecimal) = a.plus(b)
}

inline fun <T, reified V> sum(noinline extract: (T) -> V) = sum(extract, operations())

fun <T, V> sum(extract: (T) -> V, operations: Operations<V>) =
    ValueExtractor(extract) {
        it.reduce(operations::plus)
    }

inline fun <reified T> operations(): Operations<T> {
    if (T::class.java == BigDecimal::class.java) {
        @Suppress("UNCHECKED_CAST")
        return BigDecimalOperations() as Operations<T>
    }

    throw IllegalArgumentException("Type ${T::class.java} is not supported!")
}
