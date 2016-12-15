package kotlinwonders.functions.action

import kotlinwonders.Cards
import kotlinwonders.data.*
import kotlinwonders.test.OneCardSituation
import kotlinwonders.test.cardByName
import org.testng.Assert
import org.testng.Assert.assertNull
import org.testng.annotations.Test

fun getOptimalPlayerActions(cards: Cards, playerState: PlayerState, neighbors: List<PlayerState>): List<Action> =
        getOptimizableActions(cards, neighbors, playerState)
                .map { if (it is OptimizableAction) it.optimized(playerState) else it as Action }
                .filterNotNull()

fun getOptimizableActions(cards: Cards, neighbors: List<PlayerState>, playerState: PlayerState): List<ActionOrOptimizable> =
    cards.flatMap { card ->
        listOfNotNull<ActionOrOptimizable>(
                getTakeCardAction(card, playerState, neighbors),
                getBuildWonderAction(card, playerState, neighbors),
                getBurnCardAction(card))
    }

private fun getTakeCardAction(card: Card, playerState: PlayerState, neighbors: List<PlayerState>): ActionOrOptimizable? =
        if (playerState have card || playerState.gold < card.costGold)
            null
        else if ((card.prevName != null && playerState haveAny card.prev) || canHave(card.resNeeded, playerState.playerRes))
            TakeCardAction(card)
        else if (canHave(card.resNeeded, playerState.playerRes + neighbors.flatMap { it.resForNeighbors }))
            BuyCardOptimizableAction(card, getBuyResourcesVariants(card.resNeeded, playerState.playerRes, neighbors.map { it.id to it.resForNeighbors }.toMap()))
        else
            null


private fun getBuildWonderAction(card: Card, playerState: PlayerState, neighbors: List<PlayerState>): ActionOrOptimizable? =
        if (playerState.nextLevel == null)
            null
        else if (canHave(playerState.nextLevel!!.resNeeded, playerState.playerRes))
            BuildLevelAction(card)
        else if (canHave(playerState.nextLevel!!.resNeeded, playerState.playerRes + neighbors.flatMap { it.resForNeighbors }))
            BuyLevelOptimizableAction(card, getBuyResourcesVariants(playerState.nextLevel!!.resNeeded, playerState.playerRes, neighbors.map { it.id to it.resForNeighbors }.toMap()))
        else
            null

private fun getBurnCardAction(card: Card): Action = BurnCardAction(card)

class ActionFunctionsTest {

    @Test
    fun testCannotTakeCardHeHave() {
        Card.cards().forEach { c ->
            assertNull(getTakeCardAction(c, PlayerState(usedCards = listOf(c)), listOf()), "He cannot take card which he already have")
        }
    }

    @Test
    fun testCanHaveCardItHaveItsPrev() {
        Card.cards().filter { it.prevName != null }.forEach { c ->
            Assert.assertTrue(getTakeCardAction(c, PlayerState(usedCards = c.prev), listOf()) is TakeCardAction,
                    "Player should be able to take card which prev he have")
        }
    }

    @Test fun testCanHaveSimple() {
        val card = cardByName("BARRACKS")
        val state = OneCardSituation.visibleState.gameState
        val playerState = state.playersStates[0]
        val neighbors = listOf(state.playersStates[1], state.playersStates[2])
        val have = playerState.playerRes + neighbors.flatMap { it.resForNeighbors }
        assert(canHave(card.resNeeded, have))
    }

    @Test fun testTakeCardActionBuyCard() {
        val state = OneCardSituation.visibleState.gameState
        val playerState = state.playersStates[0]
        val neighbors = listOf(state.playersStates[1], state.playersStates[2])
        getTakeCardAction(cardByName("BARRACKS"), playerState, neighbors)!!
    }
}