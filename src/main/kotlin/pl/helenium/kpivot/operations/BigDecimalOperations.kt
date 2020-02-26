package pl.helenium.kpivot.operations

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN

// KF: adding extension function to each function that takes one arg end returns BigDecimal
fun <T> ((T) -> BigDecimal).sum() = sum(this, BigDecimal::plus)

fun <T> ((T) -> BigDecimal).avg() = avg(
    this,
    BigDecimal::plus,
    BigDecimal::divide
)

// KF: adding extension function to BigDecimal that is only available from within this file
private fun BigDecimal.divide(divider: Int) = divide(divider.toBigDecimal(), HALF_EVEN)
