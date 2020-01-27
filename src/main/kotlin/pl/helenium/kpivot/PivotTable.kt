package pl.helenium.kpivot

import com.google.common.collect.Lists.cartesianProduct
import java.math.BigDecimal
import java.util.UUID.randomUUID
import kotlin.random.Random

enum class Department {
    STOCKS,
    FINANCE,
    IT
}

enum class Position {
    MANAGER,
    DEVELOPER,
    TEAM_LEAD,
    QA
}

enum class Seniority {
    JUNIOR,
    REGULAR,
    SENIOR
}

data class Employee(
    val id: String,
    val department: Department,
    val position: Position,
    val seniority: Seniority,
    val salary: BigDecimal
)

// ---------------------------------------------

data class Coordinate constructor(private val fragments: List<*>) {

    constructor(vararg fragments: Any) : this(fragments.toList())

    fun allLevels() = generateSequence(this, Coordinate::parent).toList()

    private fun parent() =
        fragments
            .takeIf(Collection<*>::isNotEmpty)
            ?.dropLast(1)
            ?.let(::Coordinate)

    override fun toString() = fragments.joinToString(separator = "/", prefix = "[", postfix = "]")
}

data class MultiDimCoordinate constructor(private val coordinates: List<Coordinate>) {
    constructor(vararg coordinates: Coordinate) : this(coordinates.toList())

    fun allCombinations(): List<MultiDimCoordinate> =
        cartesianProduct(coordinates.map(Coordinate::allLevels))
            .map(::MultiDimCoordinate)

    override fun toString() = coordinates.joinToString(separator = "x")
}

typealias MultiDimCoordinateExtractor<T> = (T) -> MultiDimCoordinate

class ValueExtractor<T, V>(
    val extract: (T) -> V,
    val reduce: (Collection<V>) -> V
)

class Cell<V>(val coordinate: MultiDimCoordinate, val value: V)

fun <V> Collection<Cell<V>>.values() = map(Cell<V>::value)

class PivotTable<T, V>(
    private val items: Collection<T>,
    private val coordinateExtractor: MultiDimCoordinateExtractor<T>,
    private val valueExtractor: ValueExtractor<T, V>
) {
    fun compute() =
        items
            .flatMap(this::cells)
            .groupBy(Cell<V>::coordinate)
            .mapValues { it.value.values() }
            .mapValues { valueExtractor.reduce(it.value) }

    private fun cells(item: T): List<Cell<V>> {
        val value = valueExtractor.extract(item)
        return coordinateExtractor(item)
            .allCombinations()
            .map { Cell(it, value) }
    }
}


fun main() {
    val employees = List(40) {
        Employee(
            randomUUID().toString(),
            Department.values().random(),
            Position.values().random(),
            Seniority.values().random(),
            BigDecimal.valueOf(Random.nextLong(5_000_00, 20_000_00), 2)
        )
    }
    employees.forEach(::println)

    val pivotTable = PivotTable(
        employees,
        { e: Employee ->
            MultiDimCoordinate(
                Coordinate(e.department),
                Coordinate(e.position, e.seniority)
            )
        },
        ValueExtractor(Employee::salary) { values: Collection<BigDecimal> ->
            values.reduce(BigDecimal::plus)
        }
    )

    pivotTable
        .compute()
        .forEach { (k, v) -> println("$k -> $v")}
}
