package kotlinwonders.functions

import kotlinwonders.data.Card
import kotlinwonders.data.PlayerState
import kotlinwonders.data.getAllCardsRandomized
import kotlinwonders.data.getRandomWonder
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.utills.split

fun getStartPlayerStates(i: Int): List<PlayerState> = (0..(i - 1)).map { PlayerState(id = it, gold = 3, wonder = getRandomWonder()) }

fun getSplittedRandomStartCards(age: Int, playersNum: Int): List<List<Card>> =
        getAllCardsRandomized(age)
                .split(playersNum)
                .map { it.value.toMutableList() }

class GameStateTest {

    @Test
    fun testStartPlayerStates() {
        assert(getStartPlayerStates(3).size == 3)
        assert(getStartPlayerStates(3).all { it.gold == 3 })
    }

    @Test
    fun testRandomStartCards() {
        assert(Card.age1.size == 21) { "Jest ${Card.age3.size} a powinno być 21" }
        assert(Card.age2.size == 21) { "Jest ${Card.age3.size} a powinno być 21" }
        assert(Card.age3.size == 21) { "Jest ${Card.age3.size} a powinno być 21" }
    }

    @Test
    fun testSplittedRandomCardsNum() {
        (1..3).forEach { assert(getSplittedRandomStartCards(it, 3).all { it.size == 7 }) }
    }
}