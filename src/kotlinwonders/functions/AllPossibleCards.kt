package kotlinwonders.functions

import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import kotlinwonders.data.getAllCardsRandomized
import kotlinwonders.test.*
import org.testng.annotations.Test

fun getAllPossibleCards(gameState: GameState, knownCards: Map<Int, List<Card>>) =
        getAllCardsRandomized(gameState.age) - gameState.playersStates.flatMap { it.usedCards } - knownCards.flatMap { it.value }

class TestGetAllPossibleCards() {

    @Test fun testStartState() {
        val state = StartGameSituation.visibleState.gameState
        val cards = getAllPossibleCards(state, mapOf())
        assertEquals(cards.size, 21)
    }

    @Test fun testSomeUsedState() {
        val state = OneCardSituation.visibleState.gameState
        val cards = getAllPossibleCards(state, mapOf())
        assertTheSameSet(cards, Card.getAllAgeCards(1) - cardsByName("GUARD TOWER", "ORE VEIN", "CLAY PIT"))
        assert(cards.size == 18, { "Cards are: \n${cards.joinToString(separator = "\n")}" })
    }
}