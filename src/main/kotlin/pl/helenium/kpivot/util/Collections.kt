package pl.helenium.kpivot.util

fun <E> Collection<E>.tail() = drop(1)

fun <E> List<E>.dropLast() = dropLast(1)
