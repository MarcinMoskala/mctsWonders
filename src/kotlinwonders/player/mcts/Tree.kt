package kotlinwonders.player.mcts

import kotlinwonders.VisibleState
import kotlinwonders.data.Action
import pl.marcinmoskala.kotlindownders.utills.zeros

interface DecisionTree {
    val actionsPlanned: Map<Int, Action>
    val gameResults: List<Int>
    val visibleState: VisibleState
    fun gamesWon(id: Int): Int
    fun gamesPlayed(): Int
}

data class Leaf(
        override val visibleState: VisibleState,
        override val actionsPlanned: Map<Int, Action> = emptyMap(),
        override val gameResults: List<Int> = zeros(visibleState.playersNum)
) : DecisionTree {

//    override fun toString(): String = "GR: $gameResults, GP: ${gamesPlayed()}, A: $actionsPlanned"
    override fun gamesWon(id: Int): Int = gameResults[id]
    override fun gamesPlayed(): Int = gameResults.sum()
}

data class BranchDecisionTree(
        override val visibleState: VisibleState,
        override val actionsPlanned: Map<Int, Action> = mapOf(),
        override val gameResults: List<Int>,
        var kids: Map<Action, DecisionTree>
) : DecisionTree {

    override fun toString(): String = "Branch(GR: ${gamesWon(1)}, GP: ${gamesPlayed()}, Kids: \n${kids.toList().joinToString(separator = "\n", transform = { "    ${it.first}: ${it.second}" })})"
    override fun gamesWon(id: Int): Int = gameResults[id] + kids.map { it.value.gamesWon(id) }.sum()
    override fun gamesPlayed(): Int = gameResults.sum() + kids.map { it.value.gamesPlayed() }.sum()
}