package kotlinwonders.player.mcts

import kotlinwonders.test.assertEquals
import org.testng.annotations.Test

infix fun <T> Map<Int, List<T>>.add(map: Map<Int, List<T>>): Map<Int, List<T>> =
        (this + map).keys.map { it to ((this[it] ?: listOf()) + (map[it] ?: listOf())).distinct() }.toMap()

class AddTest() {
    @Test fun testSimpleAdding() {
        assertEquals(mapOf(1 to listOf("A")) add mapOf(2 to listOf("B")), mapOf(1 to listOf("A"), 2 to listOf("B")))
    }

    @Test fun testSimpleInsideAdding() {
        assertEquals(mapOf(1 to listOf("A")) add mapOf(1 to listOf("B")), mapOf(1 to listOf("A", "B")))
    }
}