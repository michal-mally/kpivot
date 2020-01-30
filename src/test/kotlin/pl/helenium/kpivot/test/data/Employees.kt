package pl.helenium.kpivot.test.data

import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

enum class Department {
    HR,
    LEGAL,
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

fun randomEmployee() = Employee(
    UUID.randomUUID().toString(),
    Department.values().random(),
    Position.values().random(),
    Seniority.values().random(),
    BigDecimal.valueOf(Random.nextLong(5_000_00, 20_000_00), 2)
)
