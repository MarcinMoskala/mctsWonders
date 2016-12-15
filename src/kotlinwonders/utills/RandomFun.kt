package pl.marcinmoskala.kotlindownders.utills

object Random {
    internal val gen = java.util.Random()
}

fun <E> List<E>.random(): E? = if(size == 0) null else get(Random.gen.nextInt(size))