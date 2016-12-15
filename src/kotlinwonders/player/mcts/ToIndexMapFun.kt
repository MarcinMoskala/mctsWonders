package kotlinwonders.player.mcts

import kotlinwonders.test.assertEquals
import kotlinwonders.test.assertThrowsException
import org.testng.annotations.Test

fun <E> List<E>.toIndexMap(): Map<Int, E> = mapIndexed { i, list -> i to list }.toMap()
fun <E> Map<Int, E>.toNonIndexedList(): List<E> = (0..(values.size-1)).map { get(it) ?: throw Error("No id $it in map") }

class IndexMapFunTest {

    @Test
    fun testToIndexMap() {
        assertEquals(mapOf(2 to "C", 0 to "A", 1 to "B"), listOf("A", "B", "C").toIndexMap())
    }

    @Test
    fun testToNonIndexList() {
        assertEquals(listOf("A", "B", "C"), mapOf(2 to "C", 0 to "A", 1 to "B").toNonIndexedList())
        assertThrowsException { mapOf(2 to "C", 3 to "A", 1 to "B").toNonIndexedList() }
    }
}
