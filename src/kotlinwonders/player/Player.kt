package kotlinwonders.player

import kotlinwonders.Cards
import kotlinwonders.data.Action
import kotlinwonders.data.GameState
import kotlinwonders.data.PlayerState

//TODO when it will be in kotlin i should telete it and move to full functional and maek Type for function
interface Player {
    fun makeDecision(actions: List<Action>, p: PlayerState, cards: Cards, gameState: GameState): Action
}

typealias PlayerFun = (actions: List<Action>, p: PlayerState, cards: Cards, gameState: GameState)-> Action