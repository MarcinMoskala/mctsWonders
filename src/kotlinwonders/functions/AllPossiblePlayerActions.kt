package kotlinwonders.functions

import kotlinwonders.Cards
import kotlinwonders.data.*
import kotlinwonders.functions.action.getOptimizableActions
import kotlinwonders.test.OneCardSituation
import kotlinwonders.test.assertContains
import kotlinwonders.test.cardByName
import org.testng.annotations.Test

fun getAllPossiblePlayerActions(cards: Cards, playerState: PlayerState, neighbors: List<PlayerState>): List<Action> =
        getOptimizableActions(cards, neighbors, playerState)
                .flatMap { if (it is OptimizableAction) it.allPossibilities(playerState) else listOf<Action>(it as Action) }

class TestGetAllPossiblePlayerActions {

    @Test fun testGetAllPossibleActionsForPlayerState() {
        val state = OneCardSituation.visibleState.gameState
        val possibleCards = getAllPossiblePlayerCards(0, state, mapOf())
        val playerState = state.playersStates[0]
        val neighbors = listOf(state.playersStates[1], state.playersStates[2])
        val allActions0 = getAllPossiblePlayerActions(possibleCards, playerState, neighbors)
        assertContains(BuyCardAction(cardByName("BARRACKS"), mapOf(2 to 2)), allActions0)
        assertContains(BuyLevelAction(cardByName("BARRACKS"), mapOf(2 to 2)), allActions0)
    }
}