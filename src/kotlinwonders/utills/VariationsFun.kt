package kotlinwonders.utills

import kotlinwonders.test.assertEquals
import org.testng.annotations.Test

fun <T> List<List<T>>.variations(): List<List<T>> =
        if (isEmpty() || any { it.isEmpty() }) listOf(emptyList<T>())
        else this.fold(listOf(emptyList()), { r: List<List<T>>, l: List<T> -> r.flatMap { ri -> l.map { ri + it } } })

class VariationsTest {

    @Test
    fun testStartPlayerStates() {
        val l1 = listOf(listOf("A"), listOf("B"), listOf("C"))
        assertEquals(l1.variations(), listOf(listOf("A", "B", "C")))
    }

    @Test
    fun testOneEmpty() {
        val l1 = listOf(listOf("A"), listOf("B", "C"), listOf())
        assertEquals(l1.variations(), listOf(listOf()))
    }

    @Test
    fun testAllEmpty() {
        val l1 = listOf(listOf<Any>())
        assertEquals(l1.variations(), listOf(listOf<Any>()))
    }

    @Test
    fun testTwo() {
        val l1 = listOf(listOf("A"), listOf("B", "D"), listOf("C"))
        assertEquals(l1.variations(), listOf(listOf("A", "B", "C"), listOf("A", "D", "C")))
    }

    @Test
    fun testTwoTwo() {
        val l1 = listOf(listOf("A"), listOf("B", "D"), listOf("C", "E")).variations()
        val l2 = listOf(listOf("A", "B", "C"), listOf("A", "D", "C"), listOf("A", "B", "E"), listOf("A", "D", "E"))
        assertEquals(l1.toSet(), l2.toSet())
    }
}