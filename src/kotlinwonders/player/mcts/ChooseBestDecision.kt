package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import org.testng.annotations.Test

fun DecisionTree.chooseBestDecision(id: Int): Action = when (this) {
    is Leaf -> actionsPlanned[id] ?: throw Error("Cannot find action")
    is BranchDecisionTree -> kids.chooseBest(id).key
    else -> throw Error("Cannot find action")
}

private fun Map<Action, DecisionTree>.chooseBest(id: Int) =
        maxBy { it.value.gamesWon(id) * discreditBurnCard(it.key) / it.value.gamesPlayed() }!!

class ChooseBestDecision {

    @Test
    fun testOnlyBiggest() {

    }
}