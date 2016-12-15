package pl.marcinmoskala.kotlindownders.utills

fun <E> List<E>.split(num: Int): Map<Int, List<E>> =
        mapIndexed { i, card -> i to card }
                .groupBy { it.first % num }
                .mapValues { it.value.map { it.second } }
