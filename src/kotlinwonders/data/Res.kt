package pl.marcinmoskala.kotlindownders.data

enum class Res {
    WOOD, STONE, CLAY, ORE, GLASS, PAPYRUS, LOOM;
}

infix fun Res.or(r: Res) = setOf(this, r)
infix fun Set<Res>.or(r: Res) = plus(r)