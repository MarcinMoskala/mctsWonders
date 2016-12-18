package kotlinwonders.player.mcts

import kotlinwonders.VisibleState
import kotlinwonders.data.Action
import kotlinwonders.data.getNeighbors
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.functions.getAllPossiblePlayerCards
import kotlinwonders.functions.getNextGameState
import kotlinwonders.functions.makeActionsAndThenSimulateRandomGame
import kotlinwonders.getRandomPlayers
import kotlinwonders.player.Player
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson
import pl.marcinmoskala.kotlindownders.utills.biggestPlace

fun Leaf.simulated(simulationsPerBranch: Int): BranchDecisionTree {
    val nextPlayerId = getNextPlayerId(actionsPlanned)
    return BranchDecisionTree(
            actionsPlanned = actionsPlanned,
            visibleState = visibleState,
            gameResults = gameResults,
            kids = getAllOptimalActionsForPlayerState(nextPlayerId, visibleState).map { newAction ->
                val newPlannedActions = actionsPlanned + (nextPlayerId to newAction)
                newAction to createLeaf(visibleState, newPlannedActions, simulationsPerBranch)
            }.toMap()
    )
}

private fun getAllOptimalActionsForPlayerState(id: Int, visibleState: VisibleState) =
        getOptimalPlayerActions(getAllPossiblePlayerCards(id, visibleState.gameState, visibleState.knownCards), visibleState.gameState.playersStates[id], getNeighbors(visibleState.gameState.playersStates[id], visibleState.gameState.playersStates))

private fun createLeaf(visibleState: VisibleState, actionsPlanned: Map<Int, Action>, simulationsPerBranch: Int): Leaf {
    val newVisibleState = nextVisibleState(actionsPlanned, visibleState)
    val newGameResults = getGameResult(actionsPlanned, newVisibleState, simulationsPerBranch)
    return Leaf(newVisibleState, actionsPlanned, newGameResults)
}

private fun getGameResult(actions: Map<Int, Action>, visibleState: VisibleState, simulationsPerBranch: Int): List<Int> {
    val players: List<Player> = getRandomPlayers(visibleState.gameState.playersStates.size)
    return (1..simulationsPerBranch)
            .map { makeActionsAndThenSimulateRandomGame(visibleState, actions, players) }
            .map { countFinalPoints(it.playersStates) }
            .map(::biggestPlace)
            .sumLists()
}

private fun nextVisibleState(actions: Map<Int, Action>, visibleState: VisibleState): VisibleState =
        if (visibleState.playersIds.all { it in actions.keys }) {
            val nextGameState = getNextGameState(visibleState.gameState, visibleState.playersIds.map { actions[it]!! })
            val newKnownCards = visibleState.playersIds
                    .map { visibleState.knownCards[it] ?: emptyList() }
                    .giveCardsToNextPerson(visibleState.gameState.age)
                    .filter { it.isEmpty() }
                    .toIndexMap()
            VisibleState(nextGameState, newKnownCards)
        } else visibleState

class SimulatedTest() {
    @Test fun testSimulated() {
        ExampleTree.emptyTree.simulated(4).kids.forEach { t, u -> assertEquals(u.gamesPlayed(), 4) }
    }
}