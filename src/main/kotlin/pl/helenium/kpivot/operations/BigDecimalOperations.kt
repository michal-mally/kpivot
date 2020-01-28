package pl.helenium.kpivot.operations

import java.math.BigDecimal
import java.math.RoundingMode

fun <T> ((T) -> BigDecimal).sum() = sum(this, BigDecimal::plus)

fun <T> ((T) -> BigDecimal).avg() = avg(
    this,
    BigDecimal::plus,
    bigDecimalIntDiv
)

private val bigDecimalIntDiv = { v: BigDecimal, d: Int -> v.divide(d.toBigDecimal(), RoundingMode.HALF_EVEN) }
