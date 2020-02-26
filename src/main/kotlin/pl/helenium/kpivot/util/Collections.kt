package pl.helenium.kpivot.util

// KF: adding some methods to collections that should be there from day 1
fun <E> Collection<E>.tail() = drop(1)

fun <E> List<E>.dropLast() = dropLast(1)
