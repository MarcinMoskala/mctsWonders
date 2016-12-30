package kotlinwonders

import kotlinwonders.data.getStartGameState
import kotlinwonders.functions.RealState
import kotlinwonders.functions.getSplittedRandomStartCards
import kotlinwonders.functions.simulateGame
import kotlinwonders.player.HeuristicPlayer
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
    var games = 0
    println("Zaczynamy :)")
    println("Heurisitc vs Random")
    simulateNormal(100, { HeuristicPlayer() }, { RandomPlayer() })
}

private fun simulateNormal(games: Int, getOne: () -> Player, getOthers: () -> Player) {
    val score = (1..games).fold(zeros(3)) { score, i ->
        val newScore = play(getOne(), getOthers(), getOthers())
        if(games < 50) println("Score: $newScore")
        listOf(score, newScore).sumLists()
    }
    println("Mean player score: ${score[0].toFloat() / games}, mean others: ${(score[1] + score[2]).toFloat() / (2 * games)}")
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