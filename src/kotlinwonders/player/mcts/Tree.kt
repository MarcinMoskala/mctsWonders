package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import org.testng.annotations.Test

data class VisibleState(
        val gameState: GameState,
        val knownCards: Map<Int, List<Card>>) {
    val playersNum = gameState.playersStates.size
    val playersIds = (1..(playersNum))
}

interface DecisionTree {
    val visibleState: VisibleState
    val actions: Map<Int, Action>
    val gameResults: List<Int>
    fun gamesWon(id: Int): Int
    fun gamesPlayed(): Int
}

data class Leaf(
        override val actions: Map<Int, Action>,
        override val visibleState: VisibleState,
        override val gameResults: List<Int>
) : DecisionTree {

    override fun toString(): String = "A: $actions, GR: $gameResults, GP: ${gamesPlayed()}"
    override fun gamesWon(id: Int): Int = gameResults[id]
    override fun gamesPlayed(): Int = gameResults.sum()
}

data class BranchDecisionTree(
        override val actions: Map<Int, Action>,
        override val visibleState: VisibleState,
        override val gameResults: List<Int>,
        var kids: Map<Map<Int, Action>, DecisionTree>
) : DecisionTree {

    override fun toString(): String = "Branch(GR: ${gamesWon(1)}, GP: ${gamesPlayed()}, ${kids.map { it.value }})"
    override fun gamesWon(id: Int): Int = gameResults[id] + kids.map { it.value.gamesWon(id) }.sum()
    override fun gamesPlayed(): Int = gameResults.sum() + kids.map { it.value.gamesPlayed() }.sum()
}

fun getNextPlayerId(idsOrdinary: List<Int>, actions: Map<Int, Action>): Int =
        idsOrdinary.firstOrNull { it !in actions } ?: idsOrdinary[0]