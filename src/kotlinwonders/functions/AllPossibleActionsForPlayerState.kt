package kotlinwonders.functions

import kotlinwonders.VisibleState
import kotlinwonders.data.*
import kotlinwonders.test.OneCardSituation
import kotlinwonders.test.assertContains
import kotlinwonders.test.cardByName
import org.testng.annotations.Test

fun getAllPossibleActionsForPlayerState(id: Int, visibleState: VisibleState): List<Action> {
    val possibleCards = getAllPossiblePlayerCards(id, visibleState.gameState, visibleState.knownCards)
    val playerState = visibleState.gameState.playersStates[id]
    val neighbors = getNeighbors(playerState, visibleState.gameState.playersStates)
    return getAllPossiblePlayerActions(possibleCards, playerState, neighbors)
}

class TestGetAllPossibleActionsForPlayerState {

    @Test
    fun testGetAllPossibleActionsForPlayerState() {
        val visibleState = OneCardSituation.visibleState
        val allActions0 = getAllPossibleActionsForPlayerState(0, visibleState)
        val barracksCard = cardByName("BARRACKS")
        assertContains(BuyCardAction(barracksCard, mapOf(2 to 2)), allActions0)
        assertContains(BuyLevelAction(barracksCard, mapOf(2 to 2)), allActions0)
        val allActions1 = getAllPossibleActionsForPlayerState(2, visibleState)
        assertContains(TakeCardAction(barracksCard), allActions1)
        val allActions2 = getAllPossibleActionsForPlayerState(2, visibleState)
        assertContains(TakeCardAction(barracksCard), allActions2)
    }
}