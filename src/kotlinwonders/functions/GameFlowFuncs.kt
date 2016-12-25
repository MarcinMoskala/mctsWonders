package kotlinwonders.functions

import kotlinwonders.Cards
import kotlinwonders.VisibleState
import kotlinwonders.data.*
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.getNextRoundCardsOnHand
import kotlinwonders.getRandomPlayers
import kotlinwonders.player.Player
import kotlinwonders.player.mcts.add
import org.testng.annotations.Test

fun makeActionsAndThenSimulateRandomGame(visibleState: VisibleState, actionsMap: Map<Int, Action>, players: List<Player>): GameState {
    val knownAndSupposedCards = visibleState.knownCards add actionsMap.mapValues { listOf(it.value.card) }
    val cardsOnHands = fillUnknownCardsForPlayers(knownAndSupposedCards, visibleState.gameState)
    val actions = players.indices.map { actionsMap[it] ?: getAction(visibleState.gameState.playersStates[it], players[it], visibleState.gameState, cardsOnHands[it]) }
    val newRealState = newStateForActions(RealState(visibleState.gameState, cardsOnHands), actions)
    return simulateGame(newRealState, players)
}

/*
    Function to simulateMcts game from this particular moment.
 */
tailrec fun simulateGame(realState: RealState, players: List<Player>): GameState {
    if(realState.cardsOnHands.isEmpty()) return realState.gameState
    val actions = players.indices.map { getAction(realState.gameState.playersStates[it], players[it], realState.gameState, realState.cardsOnHands[it]) }
    val newRealState = newStateForActions(realState, actions)
    return simulateGame(newRealState, players)
}

data class RealState(val gameState: GameState, val cardsOnHands: List<Cards>)

private fun newStateForActions(realState: RealState, actions: List<Action>): RealState {
    val newGameState = getNextGameState(realState.gameState, actions)
    val playersNum = newGameState.playersStates.size
    val newCardsOnHands = getNextRoundCardsOnHand(realState.gameState.round, realState.gameState.age, playersNum, actions, realState.cardsOnHands)
    return RealState(newGameState, newCardsOnHands)
}

private fun getAction(playerState: PlayerState, player: Player, gameState: GameState, cards: Cards): Action {
    val neighbors = getNeighbors(playerState, gameState.playersStates)
    val playerActions = getOptimalPlayerActions(cards, playerState, neighbors)
    return player.makeDecision(playerActions, playerState, cards, gameState)
}

class TestGameFlow() {
    @Test fun simpleTest() {
        val finalState = simulateGame(RealState(getStartGameState(3), getSplittedRandomStartCards(1, 3)), getRandomPlayers(3))
        println(finalState)
    }
}