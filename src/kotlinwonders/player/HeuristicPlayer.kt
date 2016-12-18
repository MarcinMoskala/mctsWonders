package kotlinwonders.player

import kotlinwonders.Actions
import kotlinwonders.Cards
import kotlinwonders.PlayersStates
import kotlinwonders.data.*
import kotlinwonders.functions.action.applyAction
import pl.marcinmoskala.kotlindownders.functions.countPoints
import java.util.*

class HeuristicPlayer : Player {
    val g = Random()

    fun value(a: Action, p: PlayerState, n: PlayersStates): Int {
        return when {
            a is BurnCardAction -> if(p.gold < 2) 1 else 0
            else -> p.applyAction(a, n).let { it.countPoints(n).countPoints() + it.playerRes.flatMap { it }.size } + g.nextInt(2)
        }
    }

    override fun makeDecision(actions: Actions, p: PlayerState, cards: Cards, gameState: GameState): Action {
        val n = gameState.playersStates - p
        val chosenAction = actions.maxBy { a -> value(a, p, n) }!!
//        println(chosenAction)
        return chosenAction
    }


//    sealed class Options() {
//        class BurnSomeCard: Options() {
//            override fun pointsCalc(p: PlayerState, gameState: GameState) = if(p.gold == 0) 3 else if(p.gold in 1..2) 2 else 1
//        }
//
//        class TakeCard(val c: Card): Options() {
//            override fun pointsCalc(p: PlayerState, gameState: GameState): Int = when(c.cardColour) {
//                BROWN, GREY -> c.res.flatMap { it }.count() + 1
//                BLUE -> c.points
//                RED -> c.fight + if(gameState.playersStates.any { it.fightPoints > p.fightPoints && it.fightPoints < p.fightPoints + c.fight }) 3 else 0
//                YELLOW -> if(p.gold < 3) gameState.age * 2 else gameState.age
//                GREEN -> p.c p.getGreenPoints()
//                PURPLE -> 4
//                NONE -> 5
//            }
//        }
//
//        abstract fun pointsCalc(p: PlayerState, gameState: GameState): Int
//    }
}