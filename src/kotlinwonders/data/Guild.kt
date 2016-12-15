package pl.marcinmoskala.kotlindownders.data

enum class Guild {
    COMPASS, TABLE, GEAR
}

infix fun Guild.or(r: Guild) = setOf(this, r)
infix fun Set<Guild>.or(r: Guild) = plus(r)