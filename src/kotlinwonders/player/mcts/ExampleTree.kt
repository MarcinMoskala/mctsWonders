package kotlinwonders.player.mcts

import kotlinwonders.VisibleState
import kotlinwonders.data.GameState
import kotlinwonders.data.PlayerState
import kotlinwonders.data.Wonder

object ExampleTree {

    val baseGameState = GameState(listOf(
            PlayerState(id = 0, gold = 3, wonder = Wonder.Giza),
            PlayerState(id = 1, gold = 3, wonder = Wonder.Babylon),
            PlayerState(id = 2, gold = 3, wonder = Wonder.Colosus)
    ), 1, 1)

    val emptyTree = Leaf(visibleState = VisibleState(gameState = baseGameState, knownCards = mapOf()))
}