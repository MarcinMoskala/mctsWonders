package kotlinwonders

import kotlinwonders.data.getStartGameState
import kotlinwonders.functions.RealState
import kotlinwonders.functions.getSplittedRandomStartCards
import kotlinwonders.functions.simulateGame
import kotlinwonders.player.Player
import kotlinwonders.player.RandomPlayer
import kotlinwonders.player.mcts.MctsPlayer
import kotlinwonders.player.mcts.sumLists
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints
import pl.marcinmoskala.kotlindownders.utills.zeros

private fun play(vararg players: Player): List<Int> {
    val realState = RealState(getStartGameState(players.size), getSplittedRandomStartCards(1, 3))
    val finalState = simulateGame(realState, players.toList())
    return countFinalPoints(finalState.playersStates)
}

fun main(args: Array<String>) {
    var score = zeros(3)
    var games = 0
    println("Zaczynamy :)")
    (1..100).forEach {
        val newScore = play(MctsPlayer(10, { it > 10 }), RandomPlayer(), RandomPlayer())
        score = listOf(score, newScore).sumLists()
        games ++
        println("Score: $score, Games: $games, Mean: ${score.map { it / games }}")
    }
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