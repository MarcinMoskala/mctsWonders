package kotlinwonders.player

import kotlinwonders.data.*
import pl.marcinmoskala.kotlindownders.utills.random

class RandomPlayer : Player {
    override fun makeDecision(actions: List<Action>, p: PlayerState, cards: List<Card>, gameState: GameState): Action {
        val action = actions.filter { it !is BurnCardAction }.random() ?: actions.random()!!
        return action
    }
}