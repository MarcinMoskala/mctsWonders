package kotlinwonders.functions.action

import kotlinwonders.Mines
import kotlinwonders.Resources
import org.testng.Assert
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.data.Res
import java.util.*

internal fun canHave(need: Resources, have: Mines): Boolean = when {
    need.isEmpty() -> true
    have.isEmpty() || have.size < need.size -> false
    else -> have[0]
            .filter { it in need }
            .any { r -> canHave(need.minus(r), have.drop(1)) } || canHave(need, have.drop(1))
}

class CanHaveTest {
    val rand = Random()
    val allResources = Res.values()
    val randRes: Res
        get() = allResources[rand.nextInt(allResources.size)]

    @Test
    fun testCanHave() {
        allResources.forEach { r ->
            assert(canHave(listOf(r), listOf(setOf(r))))
        }

        allResources.forEach { r ->
            assert(canHave(listOf(r, r), listOf(setOf(r), setOf(r))))
        }

        allResources.forEach { r1 ->
            allResources.forEach { r2 ->
                assert(canHave(listOf(r1, r2), listOf(setOf(r1), setOf(r2))))
            }
        }

        allResources.forEach { r ->
            assert(canHave(listOf(r, r), listOf(setOf(randRes, r), setOf(randRes, r))))
        }

        allResources.forEach { r ->
            Assert.assertFalse(canHave(listOf(r, r), listOf(setOf(r, r))))
        }

        allResources.forEach { r1 ->
            allResources.forEach { r2 ->
                Assert.assertFalse(canHave(listOf(r1, r1, r2), listOf(setOf(r1, r1), setOf(r1, r2))))
            }
        }

        assert(canHave(
                listOf(Res.WOOD, Res.WOOD, Res.STONE, Res.CLAY, Res.ORE, Res.GLASS, Res.PAPYRUS, Res.LOOM),
                listOf(
                        setOf(Res.WOOD, Res.STONE, Res.CLAY),
                        setOf(Res.STONE, Res.ORE, Res.LOOM),
                        setOf(Res.STONE, Res.WOOD, Res.ORE),
                        setOf(Res.STONE, Res.ORE, Res.GLASS),
                        setOf(Res.STONE, Res.ORE, Res.PAPYRUS),
                        setOf(Res.STONE, Res.ORE, Res.GLASS, Res.PAPYRUS),
                        setOf(Res.STONE, Res.ORE, Res.GLASS, Res.PAPYRUS),
                        setOf(Res.LOOM, Res.WOOD, Res.GLASS, Res.PAPYRUS))))
    }
}