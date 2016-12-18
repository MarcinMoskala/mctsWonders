package kotlinwonders

import kotlinwonders.data.*
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.functions.getSplittedRandomStartCards
import kotlinwonders.player.HeuristicPlayer
import kotlinwonders.player.Player
import kotlinwonders.test.assertTheSameSet
import kotlinwonders.test.cardsByName
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson

fun getNextRoundCardsOnHand(round: Int, age: Int, playersNum: Int, actions: List<Action>, prevCardsOnHands: List<Cards>) = when {
    round == roundsInAge -> getSplittedRandomStartCards(age + 1, playersNum)
    else -> prevCardsOnHands.map { it - actions.map { it.card } }.giveCardsToNextPerson(age)
}

fun getAction(playerState: PlayerState, player: Player, gameState: GameState, cards: Cards): Action {
    val neighbors = getNeighbors(playerState, gameState.playersStates)
    val playerActions = getOptimalPlayerActions(cards, playerState, neighbors)
    return player.makeDecision(playerActions, playerState, cards, gameState)
}

fun getRandomPlayers(i: Int): List<Player> = (1..i).map { HeuristicPlayer() }

class TestGetNextRoundCardsOnHand() {
    @Test fun simpleTest() {

    }

    @Test fun problematicTest() {
        val cards1 = cardsByName("LUMBER YARD", "STONE PIT")
        val cards2 = cardsByName("LOOM", "BATHS")
        val newCardsOnHands = getNextRoundCardsOnHand(5, 3, 2, actions = listOf(), prevCardsOnHands = listOf(cards1, cards2))
        assertTheSameSet(newCardsOnHands, listOf(cards2, cards1))
    }
}