package kotlinwonders

import kotlinwonders.data.getStartGameState
import kotlinwonders.functions.getSplittedRandomStartCards
import kotlinwonders.functions.simulateGame
import kotlinwonders.player.Player
import kotlinwonders.player.RandomPlayer
import kotlinwonders.player.mcts.MctsPlayer
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints

private fun play(vararg players: Player): List<Int> {
    val finalState = simulateGame(getStartGameState(players.size), getSplittedRandomStartCards(1, 3), players.toList())
    return countFinalPoints(finalState.playersStates)
}

fun main(args: Array<String>) {
    println(play(RandomPlayer(), MctsPlayer(10, { it > 100 }), RandomPlayer()))
}

class GameTest {

    @Test
    fun testRandomPlayers() {
        play(RandomPlayer(), RandomPlayer(), RandomPlayer())
    }

//    @Test
//    fun testSmartPlayer() {
//        play(RandomPlayer(), SmartPlayer(10), RandomPlayer()).let { assert(it[1] == it.max()) }
//    }
}