package kotlinwonders.data

import kotlinwonders.functions.getStartPlayerStates
import pl.marcinmoskala.kotlindownders.utills.random
import pl.marcinmoskala.kotlindownders.utills.shuffle

data class GameState(
        val playersStates: List<PlayerState>,
        val age: Int = 1,
        val round: Int = 1) {
    val isFinal: Boolean = age == ages && round == roundsInAge
}

fun getAllCardsRandomized(age: Int): List<Card> =
        Card.getAllAgeCards(age).shuffle()

const val roundsInAge = 6
const val ages = 3

fun getRandomWonder() = Wonder.values().random()!!

fun getNeighbors(p: PlayerState, list: List<PlayerState>) = list - p

fun getStartGameState(playersNum: Int) = GameState(getStartPlayerStates(playersNum))