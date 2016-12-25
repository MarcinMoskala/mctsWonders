package kotlinwonders.player

import kotlinwonders.Actions
import kotlinwonders.Cards
import kotlinwonders.PlayersStates
import kotlinwonders.data.*
import kotlinwonders.functions.action.applyAction
import pl.marcinmoskala.kotlindownders.functions.countPoints
import java.util.*

class HeuristicPlayer : Player {

    override fun makeDecision(actions: Actions, p: PlayerState, cards: Cards, gameState: GameState): Action {
        val n = gameState.playersStates - p
        val chosenAction = actions.maxBy { a -> value(a, p, n) }!!
        return chosenAction
    }

    companion object {
        val g = Random()

        fun value(a: Action, p: PlayerState, n: PlayersStates): Int = when {
            a is BurnCardAction -> if (p.gold < 2) 2 else 1
            else -> p.applyAction(a, n).let { it.countPoints(n).countPoints() + it.playerRes.flatMap { it }.size * if(it.usedCards.size < 6) 2 else 1 } + g.nextInt(2)
        }
    }
}