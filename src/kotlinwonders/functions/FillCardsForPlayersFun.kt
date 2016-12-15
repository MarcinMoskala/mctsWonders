package kotlinwonders.functions

import kotlinwonders.Cards
import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import org.testng.annotations.Test

fun fillUnknownCardsForPlayers(knownCards: Map<Int, Cards>, gameState: GameState): List<Cards> {
    var randomCards: Cards = getAllPossibleCards(gameState, knownCards)
    val cardsToHave = 8 - gameState.round
    return (0..(gameState.playersStates.size - 1)).map {
        val cardsHeHave = knownCards[it] ?: emptyList()
        val numCardsToGive = cardsToHave - (cardsHeHave.size)
        val cardsForPlayer = randomCards.take(numCardsToGive) + cardsHeHave
        randomCards = randomCards.subList(numCardsToGive, randomCards.size)
        cardsForPlayer
    }
}

class PlayGameTest {
    @Test
    fun testGetCardsForPlayers() {
        fillUnknownCardsForPlayers(mapOf(), GameState(getStartPlayerStates(3), 1, 1))
                .forEach { assert(it.size == 7, { "There is ${it.size} and should be 7" }) }
        fillUnknownCardsForPlayers(mapOf(1 to Card.cards().take(7)), GameState(getStartPlayerStates(3), 2, 1))
                .forEach { assert(it.size == 7, { "There is ${it.size} and should be 7" }) }
        fillUnknownCardsForPlayers(mapOf(
                        0 to Card.cards().take(3),
                        1 to Card.cards().take(7),
                        2 to Card.cards().take(4)), GameState(getStartPlayerStates(3), 3, 1))
                .forEach { assert(it.size == 7, { "There is ${it.size} and should be 7" }) }
        fillUnknownCardsForPlayers(mapOf(), GameState(getStartPlayerStates(3), 1, 4))
                .forEach { assert(it.size == 4, { "There is ${it.size} and should be 4" }) }
        fillUnknownCardsForPlayers(mapOf(), GameState(getStartPlayerStates(3), 2, 2))
                .forEach { assert(it.size == 6, { "There is ${it.size} and should be 6" }) }
        fillUnknownCardsForPlayers(mapOf(), GameState(getStartPlayerStates(3), 3, 6))
                .forEach { assert(it.size == 2, { "There is ${it.size} and should be 2" }) }
        fillUnknownCardsForPlayers(mapOf(), GameState(getStartPlayerStates(3), 1, 1))
                .flatMap { it }.let { assert(it.distinct() == it) }
    }
}
