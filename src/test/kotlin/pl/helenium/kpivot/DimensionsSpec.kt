package pl.helenium.kpivot

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.DayOfWeek.MONDAY

class DimensionsSpec : StringSpec({

    "should properly calculate all combinations for single dimension" {
        // given
        val dimensions = Dimensions(Dimension("String", 1, MONDAY))

        // expect
        dimensions
            .allCombinations()
            .shouldContainExactlyInAnyOrder(
                Dimensions(Dimension()),
                Dimensions(Dimension("String")),
                Dimensions(Dimension("String", 1)),
                Dimensions(Dimension("String", 1, MONDAY))
            )
    }

    "should properly calculate all combinations for multiple dimensions" {
        // given
        val dimensions = Dimensions(
            Dimension("String", 1, MONDAY),
            Dimension(1337, "Cake is a Lie")
        )

        // expect
        dimensions
            .allCombinations()
            .shouldContainExactlyInAnyOrder(
                Dimensions(
                    Dimension(),
                    Dimension()
                ),
                Dimensions(
                    Dimension(),
                    Dimension(1337)
                ),
                Dimensions(
                    Dimension(),
                    Dimension(1337, "Cake is a Lie")
                ),
                Dimensions(
                    Dimension("String"),
                    Dimension()
                ),
                Dimensions(
                    Dimension("String"),
                    Dimension(1337)
                ),
                Dimensions(
                    Dimension("String"),
                    Dimension(1337, "Cake is a Lie")
                ),
                Dimensions(
                    Dimension("String", 1),
                    Dimension()
                ),
                Dimensions(
                    Dimension("String", 1),
                    Dimension(1337)
                ),
                Dimensions(
                    Dimension("String", 1),
                    Dimension(1337, "Cake is a Lie")
                ),
                Dimensions(
                    Dimension("String", 1, MONDAY),
                    Dimension()
                ),
                Dimensions(
                    Dimension("String", 1, MONDAY),
                    Dimension(1337)
                ),
                Dimensions(
                    Dimension("String", 1, MONDAY),
                    Dimension(1337, "Cake is a Lie")
                )
            )
    }

    "should produce proper toString() representation" {
        // given
        val dimensions = Dimensions(
            Dimension("String", 1, MONDAY),
            Dimension(1337, "Cake is a Lie")
        )

        // expect
        dimensions
            .toString()
            .shouldBe("[${"String"}/${1}/${MONDAY}]x[${1337}/${"Cake is a Lie"}]")
    }

})
