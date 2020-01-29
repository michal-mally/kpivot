package pl.helenium.kpivot.operations

fun <T> ((T) -> Int).sum() = sum(this, Int::plus)

fun <T> ((T) -> Int).avg() = avg(this, Int::plus, Int::div)
