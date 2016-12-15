package pl.marcinmoskala.kotlindownders.utills

import kotlinwonders.test.assertEquals
import org.testng.annotations.Test

fun zeros(n: Int): List<Int> = (1..n).map { 0 }

class ZerosTest {
    @Test
    fun testZeros() {
        assertEquals(zeros(0), emptyList())
        assertEquals(zeros(1), listOf(0))
        assertEquals(zeros(2), listOf(0, 0))
        assertEquals(zeros(100).size, 100)
        assert(zeros(100).all { it == 0 })
    }
}