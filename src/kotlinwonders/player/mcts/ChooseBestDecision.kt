package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.player.HeuristicPlayer
import org.testng.annotations.Test

fun DecisionTree.chooseBestDecision(id: Int): Action {
    if (this !is BranchDecisionTree) throw Error("Cannot find action")
    return kids.maxBy { it.value.gamesWon(id) / it.value.gamesPlayed() }!!.key
}

fun DecisionTree.chooseBestDecisionWithHeuristic(id: Int): Action {
    if (this !is BranchDecisionTree) throw Error("Cannot find action")
    return kids.chooseBestWithHeuristic(id).key
}

private fun Map<Action, DecisionTree>.chooseBestWithHeuristic(id: Int) =
        maxBy { (action, tree) ->
            val pStates = tree.visibleState.gameState.playersStates
            HeuristicPlayer.value(action, pStates[id], pStates - pStates[id]) + 10 * (tree.gamesWon(id) / tree.gamesPlayed())
        }!!

class ChooseBestDecision {

    @Test
    fun testOnlyBiggest() {

    }
}