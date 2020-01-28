package pl.helenium.kpivot

import io.kotlintest.be
import io.kotlintest.should
import io.kotlintest.specs.StringSpec
import pl.helenium.kpivot.operations.sum
import pl.helenium.kpivot.test.data.Employee

class PivotTableSpec : StringSpec({

    "should return empty result for empty input" {
        // given
        val items = listOf<Employee>()
        val builder = items.pivot(Employee::salary.sum()) {
            dimension(it.department)
            dimension(it.position)
        }

        // when
        val table = builder.table()

        // then
        table.values.should(be(emptyMap()))
    }

})
