package kotlinwonders

import kotlinwonders.data.*
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.functions.getSplittedRandomStartCards
import kotlinwonders.player.Player
import kotlinwonders.player.RandomPlayer
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson

fun getNextRoundCardsOnHand(gameState: GameState, actions: List<Action>, prevCardsOnHands: List<Cards>) =
        if (gameState.round == roundsInAge) getSplittedRandomStartCards(gameState.age, gameState.playersStates.size)
        else prevCardsOnHands.map { it - actions.map { it.card } }.giveCardsToNextPerson(gameState.age)

fun getAction(playerState: PlayerState, player: Player, gameState: GameState, cards: Cards): Action {
    val neighbors = getNeighbors(playerState, gameState.playersStates)
    val playerActions = getOptimalPlayerActions(cards, playerState, neighbors)
    return player.makeDecision(playerActions, playerState, cards, gameState)
}

fun getRandomPlayers(i: Int): List<Player> = (1..i).map { RandomPlayer() }