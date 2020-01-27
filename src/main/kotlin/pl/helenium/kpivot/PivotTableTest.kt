package pl.helenium.kpivot

import java.math.BigDecimal
import java.util.*
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

fun main() {
    val employees = List(40) {
        Employee(
            UUID.randomUUID().toString(),
            Department.values().random(),
            Position.values().random(),
            Seniority.values().random(),
            BigDecimal.valueOf(Random.nextLong(5_000_00, 20_000_00), 2)
        )
    }
    employees.forEach(::println)

    val pivotTable = employees.pivot(sum(Employee::salary)) {
        dimension(it.department)
        dimension(it.position, it.seniority)
    }

    pivotTable
        .compute()
        .forEach { (k, v) -> println("$k -> $v") }
}
