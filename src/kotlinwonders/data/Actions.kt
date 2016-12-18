package kotlinwonders.data

import kotlinwonders.BuyVariant
import kotlinwonders.Resources
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.data.Res
import pl.marcinmoskala.kotlindownders.data.Res.CLAY
import pl.marcinmoskala.kotlindownders.data.Res.ORE

interface ActionOrOptimizable

interface OptimizableAction : ActionOrOptimizable {
    val card: Card

    //Variants are different sets of resources to buy from neighbors. We suppose that they have it.
    var buyResourcesVariants: Set<BuyVariant>

    fun optimized(playerState: PlayerState): BuyAction? =
            allPossibilities(playerState).minBy { it.cost }

    fun allPossibilities(playerState: PlayerState): List<BuyAction>
}

interface Action : ActionOrOptimizable {
    val card: Card
}

interface BuyAction : Action {
    val money: Map<Int, Int>
    val cost: Int
        get() = money.values.sum()
}


data class TakeCardAction(override val card: Card) : Action
data class BuildLevelAction(override val card: Card) : Action
data class BuyCardAction(override val card: Card, override val money: Map<Int, Int>) : BuyAction
data class BuyLevelAction(override val card: Card, override val money: Map<Int, Int>) : BuyAction
data class BurnCardAction(override val card: Card) : Action

data class BuyCardOptimizableAction(override val card: Card, override var buyResourcesVariants: Set<BuyVariant>) : OptimizableAction {
    override fun allPossibilities(playerState: PlayerState): List<BuyAction> =
            buyResourcesVariants.map { BuyCardAction(card, it.mapValues { it.toPair().countCost(playerState) }) }
                    .filter { c -> playerState.gold >= c.cost }
}

data class BuyLevelOptimizableAction(override val card: Card, override var buyResourcesVariants: Set<BuyVariant>) : OptimizableAction {
    override fun allPossibilities(playerState: PlayerState): List<BuyAction> =
            buyResourcesVariants
                    .map { BuyLevelAction(card, it.mapValues { it.toPair().countCost(playerState) }) }
                    .filter { c -> playerState.gold >= c.cost }
}

private fun Map.Entry<Int, List<Pair<Int, Res>>>.toResourcesList(): Pair<Int, Resources> = key to value.filter { it.first == key }.map { it.second }

fun Pair<Int, Resources>.countCost(playerState: PlayerState): Int = second.sumBy { playerState.buyCost[first]?.get(it) ?: 2 }

class GetTakeCardActionsTest {

    @Test
    fun testOptimizeBuyCardAction() {
        val card = Card(name = "Some")
        val action = BuyCardOptimizableAction(card, setOf(mapOf(1 to listOf(CLAY), 2 to listOf(ORE)), mapOf(1 to listOf(CLAY, CLAY))))
        val playerState = PlayerState(gold = 3, buyCost = mapOf(1 to mapOf(CLAY to 1)))
        assertEquals(action.optimized(playerState), BuyCardAction(card, mapOf(1 to 2)))
        assertEquals(action.allPossibilities(playerState).toSet(), setOf(BuyCardAction(card, mapOf(1 to 2)), BuyCardAction(card, mapOf(1 to 1, 2 to 2))))
    }
}
