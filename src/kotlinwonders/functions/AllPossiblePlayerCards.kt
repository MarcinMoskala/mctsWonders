package kotlinwonders.functions

import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import org.testng.annotations.Test

fun getAllPossiblePlayerCards(id: Int, gameState: GameState, knownCards: Map<Int, List<Card>>) =
        if(knownCards[id] != null && knownCards[id]!!.size == gameState.cardsOnHands) knownCards[id]!!
        else getAllPossibleCards(gameState, knownCards) + (knownCards[id] ?: emptyList())

class TestGetAllPossiblePlayerCards() {
    @Test fun testStartState() {

    }
}