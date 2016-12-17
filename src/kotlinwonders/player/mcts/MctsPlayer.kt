package kotlinwonders.player.mcts

import kotlinwonders.VisibleState
import kotlinwonders.data.Action
import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import kotlinwonders.data.PlayerState
import kotlinwonders.player.Player
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.utills.zeros

class MctsPlayer(val simulationsPerBranch: Int, val endCalcFun: (Int)->Boolean) : Player {

    override fun makeDecision(actions: List<Action>, p: PlayerState, cards: List<Card>, gameState: GameState): Action =
            startMcts(gameState, mapOf(p.id to cards), p.id)

    fun startMcts(gameState: GameState, knownCards: Map<Int, List<Card>>, id: Int): Action {
        require(knownCards.all { it.value.size <= 8 - gameState.round }) { "GameState: $gameState, Known Cards: $knownCards" }
        var tree: DecisionTree = Leaf(VisibleState(gameState, knownCards))
        while (!endCalcFun(tree.gamesPlayed())) {
            tree = tree.improve(simulationsPerBranch)
        }
        return tree.chooseBestDecision(id)
    }
}

class MctsPlayerTest {

    @Test
    fun testGetCardsForPlayers() {

    }
}
