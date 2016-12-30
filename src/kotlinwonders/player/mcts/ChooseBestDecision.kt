package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.player.HeuristicPlayer
import org.testng.annotations.Test

fun DecisionTree.chooseBestDecision(id: Int): Action {
    if (this !is BranchDecisionTree) throw Error("Cannot find action")
    return kids.maxBy { it.value.gamesWon(id) / it.value.gamesPlayed() }!!.key
}

class ChooseBestDecision {

    @Test
    fun testOnlyBiggest() {

    }
}