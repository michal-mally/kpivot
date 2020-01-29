package pl.helenium.kpivot

import io.kotlintest.be
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
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

    "should return all keys for dimension at existing index" {
        // given
        val items = listOf(
            1 to "One",
            2 to "Two"
        )

        // when
        val table = items.pivot((Pair<Int, String>::first).sum()) {
            dimension(it.first, it.second)
            dimension(it.second, it.first)
        }.table()

        // then
        table
            .keysForDimension(0)
            .shouldContainExactlyInAnyOrder(
                Dimension(1, "One"),
                Dimension(1),
                Dimension(2, "Two"),
                Dimension(2),
                Dimension()
            )

        // and
        table
            .keysForDimension(1)
            .shouldContainExactlyInAnyOrder(
                Dimension("One", 1),
                Dimension("One"),
                Dimension("Two", 2),
                Dimension("Two"),
                Dimension()
            )
    }

    "should return empty keys for dimension at non-existent index" {
        // given
        val items = listOf(
            1 to "One",
            2 to "Two"
        )

        // when
        val table = items.pivot((Pair<Int, String>::first).sum()) {
            dimension(it.first, it.second)
            dimension(it.second, it.first)
        }.table()

        // then
        table
            .keysForDimension(2)
            .shouldBeEmpty()
    }

})
