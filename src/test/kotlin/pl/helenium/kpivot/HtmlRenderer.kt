package pl.helenium.kpivot

import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr
import pl.helenium.kpivot.operations.sum
import pl.helenium.kpivot.test.data.Employee
import pl.helenium.kpivot.test.data.randomEmployee

class HtmlRenderer(private val pivotTable: PivotTable<*>) {

    fun render() {
        val rows = pivotTable.keysForDimension(0)
        val columns = pivotTable.keysForDimension(1)

        System.out.appendHTML().html {
            body {
                table {
                    tr {
                        th { }
                        columns.forEach { d ->
                            th { +d.toString() }
                        }
                    }
                    rows.forEach { r ->
                        tr {
                            td { +r.toString() }
                            columns.forEach { c ->
                              td { +(pivotTable.values[Dimensions(r, c)]?.toString() ?: "") }
                            }
                        }
                    }
                }
            }
        }
    }

}

fun main() {
    val employees = List(40) { randomEmployee() }
    val builder = employees.pivot(Employee::salary.sum()) {
        dimension(it.position, it.seniority)
        dimension(it.department)
    }
    HtmlRenderer(builder.table()).render()

}
