package kotlinwonders.functions

import kotlinwonders.Cards
import kotlinwonders.data.*
import kotlinwonders.functions.action.applyAction
import kotlinwonders.getAction
import kotlinwonders.getNextRoundCardsOnHand
import kotlinwonders.getRandomPlayers
import kotlinwonders.player.Player
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.giveFightPoints

fun makeActionsAndThenSimulateRandomGame(gameState: GameState, knownCards: Map<Int, Cards>, actions: Map<Int, Action>): GameState {
    val players: List<Player> = getRandomPlayers(gameState.playersStates.size)
    val cardsOnHands = fillUnknownCardsForPlayers(knownCards add actions.mapValues { listOf(it.value.card) }, gameState)
    val realActions = players.indices.map { actions[it] ?: getAction(gameState.playersStates[it], players[it], gameState, cardsOnHands[it]) }
    return simulateForActions(realActions, cardsOnHands, gameState, players)
}

fun cardsNumInRound(round: Int): Int = 8 - round

infix fun <T> Map<Int, List<T>>.add(map: Map<Int, List<T>>): Map<Int, List<T>> =
        mapValues { (it.value + (map[it.key] ?: emptyList())).distinct() }

fun simulateGame(gameState: GameState, cardsOnHands: List<Cards>, players: List<Player>): GameState {
    val actions = players.indices.map { getAction(gameState.playersStates[it], players[it], gameState, cardsOnHands[it]) }
    return simulateForActions(actions, cardsOnHands, gameState, players)
}

fun getNextGameState(state: GameState, actions: List<Action>): GameState {
    val playersStates = state.playersStates
            .applyActions(actions)
            .giveMoneyFromTranactions(actions)
            .let { if (state.round == roundsInAge) it.giveFightPoints(state.age) else it }

    val (nextAge, nextRound) = nextAgeAndRound(state.age, state.round)
    return GameState(playersStates, nextAge, nextRound)
}

fun Map<Int, List<Card>>.removeUsedCards(allActions: List<Action>) = mapValues { it.value - allActions.map { it.card } }

private fun  List<PlayerState>.applyActions(actions: List<Action>): List<PlayerState> =
        mapIndexed { i, player -> player.applyAction(actions[i], getNeighbors(player, this)) }

fun List<PlayerState>.giveMoneyFromTranactions(actions: List<Action>): List<PlayerState> = actions.fold(this) { playerStates, action ->
    if (action is BuyAction) playerStates.map { p -> if (p.id in action.money) p.copy(gold = p.gold + action.money[p.id]!!) else p }
    else playerStates
}

private fun simulateForActions(actions: List<Action>, cardsOnHands: List<Cards>, gameState: GameState, players: List<Player>): GameState {
    return if (gameState.isFinal) getNextGameState(gameState, actions)
    else simulateGame(
            gameState = getNextGameState(gameState, actions),
            cardsOnHands = getNextRoundCardsOnHand(gameState, actions, cardsOnHands),
            players = players)
}

private fun nextAgeAndRound(age: Int, round: Int): Pair<Int, Int> =
        if (age == ages && round == roundsInAge) age to round
        else Pair(if (round == roundsInAge) age + 1 else age, round % roundsInAge + 1)

class GameFlowFuncs() {
    @Test
    fun testNextAgeAndRound() {
        assertEquals(nextAgeAndRound(1, 1), Pair(1, 2))
        assertEquals(nextAgeAndRound(2, 1), Pair(2, 2))
        assertEquals(nextAgeAndRound(3, 1), Pair(3, 2))
        assertEquals(nextAgeAndRound(1, roundsInAge), Pair(2, 1))
        assertEquals(nextAgeAndRound(1, roundsInAge - 1), Pair(1, roundsInAge))
        assertEquals(nextAgeAndRound(2, roundsInAge), Pair(3, 1))
    }
}