package pl.marcinmoskala.kotlindownders.utills

import kotlinwonders.test.assertEquals
import org.testng.annotations.Test

fun biggestPlace(l: List<Int>): List<Int> = l.map { l.max() == it }.map { if (it) 1 else 0 }

class BiggestTest {

    @Test
    fun testOnlyBiggest() {
        assertEquals(biggestPlace(listOf(5)), listOf(1))
        assertEquals(biggestPlace(listOf(5, 4)), listOf(1, 0))
        assertEquals(biggestPlace(listOf(5, 4, 6)), listOf(0, 0, 1))
        assertEquals(biggestPlace(listOf(5, 4, 6, 100000)), listOf(0, 0, 0, 1))
    }
}