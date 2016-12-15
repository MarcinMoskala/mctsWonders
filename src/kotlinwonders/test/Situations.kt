package kotlinwonders.test

import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import kotlinwonders.data.PlayerState
import kotlinwonders.data.Wonder
import kotlinwonders.VisibleState

object StartGameSituation {
    val visibleState: VisibleState
        get() {
            val s = VisibleState(GameState(listOf(
                    PlayerState(id = 0, gold = 3, wonder = Wonder.Babylon),
                    PlayerState(id = 1, gold = 3, wonder = Wonder.Colosus),
                    PlayerState(id = 2, gold = 3, wonder = Wonder.Giza))), mapOf())
            s.gameState.playersStates.forEach { assert(it.usedCards.isEmpty()) }
            return s
        }
}

object OneCardSituation {
    val visibleState: VisibleState
        get() {
            val s = VisibleState(GameState(listOf(
                    PlayerState(id = 0, gold = 3, wonder = Wonder.Babylon, usedCards = cardsByName("GUARD TOWER")),
                    PlayerState(id = 1, gold = 3, wonder = Wonder.Colosus, usedCards = cardsByName("ORE VEIN")),
                    PlayerState(id = 2, gold = 3, wonder = Wonder.Giza, usedCards = cardsByName("CLAY PIT")))), mapOf())
            s.gameState.playersStates.forEach { assert(it.usedCards.size == 1) }
            val restOfCards = Card.getAllAgeCards(1) - cardsByName("GUARD TOWER", "ORE VEIN", "CLAY PIT")
            s.gameState.playersStates.forEach { assert(it.usedCards.none { it in restOfCards }) }
            return s
        }
}