package pl.marcinmoskala.kotlindownders.functions

import kotlinwonders.player.mcts.toIndexMap
import kotlinwonders.player.mcts.toNonIndexedList
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test

fun <T> List<T>.giveCardsToNextPerson(age: Int): List<T> =
        if (age == 2) this.takeLast(size - 1) + this[0]
        else listOf(this.last()) + this.take(size - 1)

fun <T> Map<Int, List<T>>.giveCardsToNextPerson(age: Int): Map<Int, List<T>> =
        this.toNonIndexedList()
                .giveCardsToNextPerson(age)
                .toIndexMap()

class MapIndexedAndListTest() {

    @Test
    fun testListGiveCardsToNextPerson() {
        assertEquals(listOf("A", "B", "C").giveCardsToNextPerson(1), listOf("C", "A", "B"))
        assertEquals(listOf("A", "B", "C").giveCardsToNextPerson(2), listOf("B", "C", "A"))
        assertEquals(listOf("A", "B", "C").giveCardsToNextPerson(3), listOf("C", "A", "B"))
        fun card(vararg i: Int) = i.map { kotlinwonders.data.Card.age1[it] }
        assertEquals(listOf(card(0), card(1), card(2)).giveCardsToNextPerson(3), listOf(card(2), card(0), card(1)))
        assertEquals(listOf(card(), card(1), card(0, 2)).giveCardsToNextPerson(3), listOf(card(0, 2), card(), card(1)))
    }

    @Test
    fun testMapGiveCardsToNextPerson() {
        fun card(vararg i: Int) = i.map { kotlinwonders.data.Card.age1[it] }
        assertEquals(mapOf(0 to card(0), 1 to card(1), 2 to card(2)).giveCardsToNextPerson(3), mapOf(0 to card(2), 1 to card(0), 2 to card(1)))
        assertEquals(mapOf(0 to card(), 1 to card(1), 2 to card(0, 2)).giveCardsToNextPerson(3), mapOf(0 to card(0, 2), 1 to card(), 2 to card(1)))
        assertEquals(mapOf(0 to card(0), 2 to card(2), 1 to card(1)).giveCardsToNextPerson(3), mapOf(2 to card(1), 0 to card(2), 1 to card(0)))
    }
}