package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.player.Player
import org.testng.annotations.Test

fun DecisionTree.improve(simulationsPerBranch: Int, players: List<Player>): DecisionTree = when (this) {
    is Leaf -> simulated(simulationsPerBranch, players)
    is BranchDecisionTree -> {
        val next = kids.chooseNext(getNextPlayerId(actionsPlanned))
        copy(kids = kids.plus(next.key to next.value.improve(simulationsPerBranch, players)))
    }
    else -> throw Error("Cannot find element type")
}

private fun Map<Action, DecisionTree>.chooseNext(id: Int): Map.Entry<Action, DecisionTree> {
    val gamesPlayed = map { it.value.gamesPlayed() }.sum()
    return maxBy { ((it.value.gamesWon(id).toFloat() / it.value.gamesPlayed())) + Math.sqrt(2.0 * gamesPlayed / it.value.gamesPlayed()) }!!
}

fun getNextPlayerId(actionsPlanned: Map<Int, Action>): Int = actionsPlanned.size % 3