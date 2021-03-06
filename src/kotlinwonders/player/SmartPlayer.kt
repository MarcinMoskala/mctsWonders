package kotlinwonders.player

import kotlinwonders.VisibleState
import kotlinwonders.data.*
import kotlinwonders.functions.makeActionsAndThenSimulateRandomGame
import kotlinwonders.getRandomPlayers
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints
import pl.marcinmoskala.kotlindownders.utills.random

class SmartPlayer(val simulationsNumber: Int) : Player {

    override fun makeDecision(actions: List<Action>, p: PlayerState, cards: List<Card>, gameState: GameState): Action =
            actions.filter { it !is BurnCardAction }
                    .maxBy { simulationsSumResult(it, p, cards, gameState) }
                    ?: actions.random()!!

    private fun simulationsSumResult(a: Action, p: PlayerState, cards: List<Card>, s: GameState): Int =
            (1..simulationsNumber).sumBy { simulate(a, p, s, cards).toScore(p.id) }

    private fun List<Int>.toScore(id: Int): Int = get(id) * 3 - sum()

    private fun simulate(a: Action, p: PlayerState, gameState: GameState, cards: List<Card>): List<Int> {
        val players: List<Player> = getRandomPlayers(gameState.playersStates.size)
        val finalState = makeActionsAndThenSimulateRandomGame(VisibleState(gameState, mapOf(p.id to cards)), mapOf(p.id to a), players)
        return countFinalPoints(finalState.playersStates)
    }
}