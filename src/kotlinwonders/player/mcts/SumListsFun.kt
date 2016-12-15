package kotlinwonders.player.mcts

import kotlinwonders.test.assertEquals
import kotlinwonders.test.assertThrowsException
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.utills.zeros

fun List<List<Int>>.sumLists(): List<Int> = fold(zeros(this[0].size), { l1, l2 -> l1.indices.map { l2[it] + l1[it] } })

class SumListsFunTest {

    @Test
    fun testSumLists() {
        assertEquals(listOf(2, 4, 6), listOf(listOf(1,2,3), listOf(1,2,3)).sumLists())
        assertEquals(listOf(3, 6, 9), listOf(listOf(1,2,3), listOf(1,2,3), listOf(1,2,3)).sumLists())
        assertThrowsException { listOf(listOf(1,2,3), listOf(1)).sumLists() }
    }
}
