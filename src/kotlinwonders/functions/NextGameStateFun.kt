package kotlinwonders.functions

import kotlinwonders.data.*
import kotlinwonders.functions.action.applyAction
import kotlinwonders.test.*
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.giveFightPoints

fun getNextGameState(state: GameState, actions: List<Action>): GameState {
    val playersStates = state.playersStates
            .applyActions(actions)
            .giveMoneyFromTransactions(actions)
            .let { if (state.round == roundsInAge) it.giveFightPoints(state.age) else it }

    val (nextAge, nextRound) = nextAgeAndRound(state.age, state.round)
    return GameState(playersStates, nextAge, nextRound)
}

fun Map<Int, List<Card>>.removeUsedCards(allActions: List<Action>) = mapValues { it.value - allActions.map { it.card } }

private fun List<PlayerState>.applyActions(actions: List<Action>): List<PlayerState> =
        mapIndexed { i, player -> player.applyAction(actions[i], getNeighbors(player, this)) }

fun List<PlayerState>.giveMoneyFromTransactions(actions: List<Action>): List<PlayerState> = actions.fold(this) { playerStates, action ->
    if (action is BuyAction) playerStates.map { p -> if (p.id in action.money) p.copy(gold = p.gold + action.money[p.id]!!) else p }
    else playerStates
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

    @Test
    fun testNextGameState() {
        val startState = StartGameSituation.visibleState.gameState
        val actions = listOf(TakeCardAction(cardByName("LUMBER YARD")), BurnCardAction(cardByName("CLAY POOL")), TakeCardAction(cardByName("CLAY PIT")))
        val newState = getNextGameState(startState, actions)
        val playerStates = newState.playersStates
        assertTheSame(playerStates[0].usedCards, cardsByName("LUMBER YARD"))
        assertTheSame(playerStates[1].usedCards, emptyList())
        assertTheSame(playerStates[2].usedCards, cardsByName("CLAY PIT"))
        assertEquals(playerStates[2].gold, 2)
    }
}