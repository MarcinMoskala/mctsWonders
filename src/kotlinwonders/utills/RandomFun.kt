package pl.marcinmoskala.kotlindownders.utills

import java.util.*

private val gen = Random()

fun <E> List<E>.random(seed: Long = gen.nextLong()): E? = if(size == 0) null else get(Random(seed).nextInt(size))

fun <T> List<T>.shuffle(seed: Long = gen.nextLong()): List<T> {
    val a = toMutableList()
    var n = a.size
    while (n > 1) {
        val k = Random(seed).nextInt(n--)
        val t = a[n]
        a[n] = a[k]
        a[k] = t
    }
    return a
}