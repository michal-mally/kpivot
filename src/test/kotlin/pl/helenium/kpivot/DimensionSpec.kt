package pl.helenium.kpivot

import io.kotlintest.be
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import io.kotlintest.specs.StringSpec
import java.time.DayOfWeek.MONDAY

class DimensionSpec : StringSpec({

    "two dimensions with the same values should equal" {
        // given
        val dim1 = Dimension("String", 1, MONDAY)
        val dim2 = Dimension("String", 1, MONDAY)

        // expect
        dim1.shouldBe(dim2)
    }

    "two dimensions with different values should not equal" {
        // given
        val dim1 = Dimension("String", 1, MONDAY)
        val dim2 = Dimension("String", 2, MONDAY)

        // expect
        dim1.shouldNot(be(dim2))
    }

    "should properly calculate all levels" {
        // given
        val dimension = Dimension("String", 1, MONDAY)

        // expect
        dimension
            .allLevels()
            .shouldContainExactlyInAnyOrder(
                Dimension(),
                Dimension("String"),
                Dimension("String", 1),
                Dimension("String", 1, MONDAY)
            )
    }

    "should produce proper toString representation" {
        // given
        val dimension = Dimension("String", 1, MONDAY)

        // expect
        dimension
            .toString()
            .shouldBe("[${"String"}/${1}/${MONDAY}]")
    }

})
