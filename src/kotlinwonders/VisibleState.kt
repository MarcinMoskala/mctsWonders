package kotlinwonders

import kotlinwonders.data.Card
import kotlinwonders.data.GameState

data class VisibleState(
        val gameState: GameState,
        val knownCards: Map<Int, List<Card>>) {
    val playersNum = gameState.playersStates.size
    val playersIds = (1..(playersNum))
}