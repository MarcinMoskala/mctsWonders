package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import org.testng.annotations.Test

fun DecisionTree.chooseBestDecision(id: Int): Action = when (this) {
    is Leaf -> actions[id] ?: throw Error("Cannot find action")
    is BranchDecisionTree -> kids.chooseBest(id).key[id]!!
    else -> throw Error("Cannot find action")
}

private fun Map<Map<Int, Action>, DecisionTree>.chooseBest(id: Int) =
        maxBy { it.value.gamesWon(id) * discreditBurnCard(it.key[id]) / it.value.gamesPlayed() }!!

class ChooseBestDecision {

    @Test
    fun testOnlyBiggest() {

    }
}