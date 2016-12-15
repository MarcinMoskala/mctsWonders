package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.data.Card
import kotlinwonders.data.getNeighbors
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.functions.getAllPossiblePlayerCards
import kotlinwonders.functions.getNextGameState
import kotlinwonders.functions.makeActionsAndThenSimulateRandomGame
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson
import pl.marcinmoskala.kotlindownders.utills.biggestPlace

fun DecisionTree.improve(idsOrdinary: List<Int>, simulationsPerBranch: Int): DecisionTree = when (this) {
    is Leaf -> simulated(idsOrdinary, simulationsPerBranch)
    is BranchDecisionTree -> {
        val next = kids.chooseNext(getNextPlayerId(idsOrdinary, actions))
        copy(kids = kids.plus(next.key to next.value.improve(idsOrdinary, simulationsPerBranch)))
    }
    else -> throw Error("Cannot find element type")
}

private fun Map<Map<Int, Action>, DecisionTree>.chooseNext(id: Int): Map.Entry<Map<Int, Action>, DecisionTree> {
    val gamesPlayed = map { it.value.gamesPlayed() }.sum()
    return maxBy { ((it.value.gamesWon(id).toFloat() / it.value.gamesPlayed()) * discreditBurnCard(it.key[id])) + Math.sqrt(2.0 * gamesPlayed / it.value.gamesPlayed()) }!!
}

fun Leaf.simulated(idsOrdinary: List<Int>, simulationsPerBranch: Int): BranchDecisionTree {
    val nextPlayerId = getNextPlayerId(idsOrdinary, actions)
    return BranchDecisionTree(
            visibleState = visibleState,
            gameResults = gameResults,
            kids = getAllOptimalActionsForPlayerState(nextPlayerId, visibleState).map { newAction ->
                val newActions = actions + (nextPlayerId to newAction)
                newActions to createLeaf(visibleState, visibleState.knownCards, newActions, simulationsPerBranch)
            }.toMap()
    )
}

private fun getAllOptimalActionsForPlayerState(id: Int, visibleState: VisibleState) =
        getOptimalPlayerActions(getAllPossiblePlayerCards(id, visibleState.gameState, visibleState.knownCards), visibleState.gameState.playersStates[id], getNeighbors(visibleState.gameState.playersStates[id], visibleState.gameState.playersStates))

private fun createLeaf(visibleState: VisibleState, knownCards: Map<Int, List<Card>>, actions: Map<Int, Action>, simulationsPerBranch: Int) = Leaf(
        actions = actions,
        visibleState = nextVisibleState(actions, knownCards, visibleState),
        gameResults = getGameResult(actions, knownCards, visibleState, simulationsPerBranch)
)

private fun getGameResult(actions: Map<Int, Action>, knownCards: Map<Int, List<Card>>, visibleState: VisibleState, simulationsPerBranch: Int): List<Int> =
        (1..simulationsPerBranch)
                .map { makeActionsAndThenSimulateRandomGame(visibleState.gameState, knownCards, actions) }
                .map { countFinalPoints(it.playersStates) }
                .map(::biggestPlace)
                .sumLists()

private fun nextVisibleState(actions: Map<Int, Action>, knownCards: Map<Int, List<Card>>, visibleState: VisibleState): VisibleState =
        if (visibleState.playersIds.all { it in actions.keys })
            VisibleState(
                    getNextGameState(visibleState.gameState, visibleState.playersIds.map { actions[it]!! }),
                    visibleState.playersIds.map { knownCards[it] ?: emptyList() }.giveCardsToNextPerson(visibleState.gameState.age).filter { it.isEmpty() }.toIndexMap()
            )
        else visibleState