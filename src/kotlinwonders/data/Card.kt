package kotlinwonders.data

import kotlinwonders.Cards
import kotlinwonders.Mines
import kotlinwonders.Resources
import pl.marcinmoskala.kotlindownders.data.CardColour
import pl.marcinmoskala.kotlindownders.data.CardColour.*
import pl.marcinmoskala.kotlindownders.data.Guild
import pl.marcinmoskala.kotlindownders.data.Guild.*
import pl.marcinmoskala.kotlindownders.data.Res.*
import pl.marcinmoskala.kotlindownders.data.or

data class Card(
        val age: Int? = null,
        val name: String = "",
        val resNeeded: Resources = listOf(),
        val res: Mines = listOf(),
        val guild: Set<Guild> = setOf(),
        val prevName: String? = null,
        val points: Int = 0,
        val fight: Int = 0,
        val extraGold: Int = 0,
        val costGold: Int = 0,
        val cardColour: CardColour = NONE,
        val specialAction: ((PlayerState, List<PlayerState>) -> PlayerState)? = null,
        val endGameSpecialPoints: ((PlayerState, List<PlayerState>) -> Int)? = null
) {
    override fun toString(): String = name
    val prev by lazy { if(prevName != null) (age1 + age2).filter { prevName.toRegex() in it.name } else emptyList() }

    companion object {
        val age1 = listOf(
                Card(age = 1, name = "LUMBER YARD", res = listOf(setOf(WOOD)), cardColour = BROWN),
                Card(age = 1, name = "STONE PIT", res = listOf(setOf(STONE)), cardColour = BROWN),
                Card(age = 1, name = "CLAY POOL", res = listOf(setOf(CLAY)), cardColour = BROWN),
                Card(age = 1, name = "ORE VEIN", res = listOf(setOf(ORE)), cardColour = BROWN),
                Card(age = 1, name = "CLAY PIT", costGold = 1, res = listOf(ORE or CLAY), cardColour = BROWN),
                Card(age = 1, name = "TIMBER YARD", costGold = 1, res = listOf(WOOD or STONE), cardColour = BROWN),
                Card(age = 1, name = "LOOM", res = listOf(setOf(LOOM)), cardColour = GREY),
                Card(age = 1, name = "GLASSWORKS", res = listOf(setOf(GLASS)), cardColour = GREY),
                Card(age = 1, name = "PRESS", res = listOf(setOf(PAPYRUS)), cardColour = GREY),
                Card(age = 1, name = "BATHS", resNeeded = listOf(STONE), points = 3, cardColour = BLUE),
                Card(age = 1, name = "ALTAR", points = 2, cardColour = BLUE),
                Card(age = 1, name = "THEATER", points = 2, cardColour = BLUE),
                Card(age = 1, name = "EAST TRADING POST", specialAction = { player, neighbour -> player.copy(buyCost = player.buyCost + (neighbour[0].id to mapOf(CLAY to 1, WOOD to 1, STONE to 1, ORE to 1))) }, cardColour = YELLOW), //TODO
                Card(age = 1, name = "WEST TRADING POST", specialAction = { player, neighbour -> player.copy(buyCost = player.buyCost + (neighbour[1].id to mapOf(CLAY to 1, WOOD to 1, STONE to 1, ORE to 1))) }, cardColour = YELLOW), //TODO
                Card(age = 1, name = "MARKETPLACE", specialAction = { player, neighbour -> player.copy(buyCost = player.buyCost + (neighbour[0].id to mapOf(GLASS to 1, LOOM to 1, PAPYRUS to 1)) + (neighbour[1].id to mapOf(GLASS to 1, LOOM to 1, PAPYRUS to 1))) }, cardColour = YELLOW), //TODO
                Card(age = 1, name = "STOCKADE", resNeeded = listOf(WOOD), fight = 1, cardColour = RED),
                Card(age = 1, name = "BARRACKS", resNeeded = listOf(ORE), fight = 1, cardColour = RED),
                Card(age = 1, name = "GUARD TOWER", resNeeded = listOf(CLAY), fight = 1, cardColour = RED),
                Card(age = 1, name = "APOTHECARY", resNeeded = listOf(LOOM), guild = setOf(COMPASS), cardColour = GREEN),
                Card(age = 1, name = "WORKSHOP", resNeeded = listOf(LOOM), guild = setOf(GEAR), cardColour = GREEN),
                Card(age = 1, name = "SCRIPTORIUM", resNeeded = listOf(GLASS), guild = setOf(TABLE), cardColour = GREEN)
        )

        val age2 = listOf(
                Card(age = 2, name = "SAWMILL", costGold = 1, res = listOf(setOf(WOOD), setOf(WOOD)), cardColour = BROWN),
                Card(age = 2, name = "QUARRY", costGold = 1, res = listOf(setOf(STONE), setOf(STONE)), cardColour = BROWN),
                Card(age = 2, name = "BRICKYARD", costGold = 1, res = listOf(setOf(CLAY), setOf(CLAY)), cardColour = BROWN),
                Card(age = 2, name = "FOUNDRY", costGold = 1, res = listOf(setOf(ORE), setOf(ORE)), cardColour = BROWN),
                Card(age = 2, name = "LOOM", res = listOf(setOf(LOOM)), cardColour = GREY),
                Card(age = 2, name = "GLASSWORKS", res = listOf(setOf(GLASS)), cardColour = GREY),
                Card(age = 2, name = "PRESS", res = listOf(setOf(PAPYRUS)), cardColour = GREY),
                Card(age = 2, name = "AQUEDUCT", resNeeded = listOf(STONE, STONE, STONE), points = 5, prevName = "BATHS", cardColour = BLUE),
                Card(age = 2, name = "TEMPLE", resNeeded = listOf(WOOD, CLAY, GLASS), points = 3, prevName = "ALTAR", cardColour = BLUE),
                Card(age = 2, name = "STATUE", resNeeded = listOf(WOOD, ORE, ORE), points = 4, prevName = "THEATER", cardColour = BLUE),
                Card(age = 2, name = "FORUM", resNeeded = listOf(CLAY, CLAY), res = listOf(LOOM or GLASS or PAPYRUS), prevName = "TRADING POST", cardColour = YELLOW),
                Card(age = 2, name = "CARAVANSERY", resNeeded = listOf(WOOD, WOOD), res = listOf(STONE or WOOD or ORE or CLAY), prevName = "MARKETPLACE", cardColour = YELLOW),
                Card(age = 2, name = "VINEYARD", specialAction = { p, n -> p.copy(gold = p.gold + (n + p).flatMap { it.usedCards }.count { it.cardColour == BROWN }) }, cardColour = YELLOW),
                Card(age = 2, name = "WALLS", resNeeded = listOf(STONE, STONE, STONE), fight = 2, cardColour = RED),
                Card(age = 2, name = "STABLES", resNeeded = listOf(WOOD, ORE, CLAY), fight = 2, prevName = "APOTHECARY", cardColour = RED),
                Card(age = 2, name = "ARCHERY RANGE", resNeeded = listOf(WOOD, ORE, WOOD), fight = 2, prevName = "WORKSHOP", cardColour = RED),
                Card(age = 2, name = "DISPENSARY", resNeeded = listOf(ORE, ORE, GLASS), guild = setOf(COMPASS), prevName = "APOTHECARY", cardColour = GREEN),
                Card(age = 2, name = "LABORATORY", resNeeded = listOf(CLAY, CLAY, PAPYRUS), guild = setOf(GEAR), prevName = "WORKSHOP", cardColour = GREEN),
                Card(age = 2, name = "COURTHOUSE", resNeeded = listOf(CLAY, CLAY, LOOM), points = 4, prevName = "SCRIPTORIUM", cardColour = BLUE),
                Card(age = 2, name = "LIBRARY", resNeeded = listOf(STONE, STONE, LOOM), guild = setOf(TABLE), prevName = "SCRIPTORIUM", cardColour = GREEN),
                Card(age = 2, name = "SCHOOL", resNeeded = listOf(WOOD, PAPYRUS), guild = setOf(TABLE), cardColour = GREEN)
        )

        val age3 = listOf(
                Card(age = 3, name = "WORKERS GUILD", resNeeded = listOf(ORE, ORE, CLAY, STONE, WOOD), endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.filter { it.cardColour == BROWN }.count() }, cardColour = PURPLE),
                Card(age = 3, name = "CRAFTSMENS GUILD", resNeeded = listOf(ORE, ORE, STONE, WOOD), endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.filter { it.cardColour == GREY }.count() * 2 }, cardColour = PURPLE),
                Card(age = 3, name = "TRADERS GUILD", resNeeded = listOf(PAPYRUS, GLASS, LOOM), endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.filter { it.cardColour == YELLOW }.count() }, cardColour = PURPLE),
                Card(age = 3, name = "PHILOSOPHERS GUILD", resNeeded = listOf(CLAY, CLAY, CLAY, PAPYRUS, LOOM), endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.filter { it.cardColour == GREEN }.count() }, cardColour = PURPLE),
                Card(age = 3, name = "SPIES GUILD", resNeeded = listOf(CLAY, CLAY, CLAY, GLASS), endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.filter { it.cardColour == RED }.count() }, cardColour = PURPLE),
                Card(age = 3, name = "PANTHEON", resNeeded = listOf(CLAY, CLAY, ORE, PAPYRUS, LOOM), points = 7, prevName = "TEMPLE", cardColour = BLUE),
                Card(age = 3, name = "GARDENS", resNeeded = listOf(CLAY, CLAY, WOOD), points = 5, prevName = "STATUE", cardColour = BLUE),
                Card(age = 3, name = "TOWN HALL", resNeeded = listOf(STONE, STONE, ORE, GLASS), points = 6, cardColour = BLUE),
                Card(age = 3, name = "PALACE", resNeeded = listOf(STONE, ORE, WOOD, CLAY, GLASS, LOOM, PAPYRUS), points = 8, cardColour = BLUE),
                Card(age = 3, name = "SENATE", resNeeded = listOf(ORE, STONE, WOOD, WOOD), points = 6, prevName = "LIBRARY", cardColour = BLUE),
                Card(age = 3, name = "HAVEN", resNeeded = listOf(LOOM, ORE, WOOD), prevName = "FORUM", specialAction = { p, n -> p.copy(gold = p.gold + p.usedCards.count { it.cardColour == BROWN }) }, endGameSpecialPoints = { p, n -> p.usedCards.count { it.cardColour == BROWN } }, cardColour = YELLOW),
                Card(age = 3, name = "LIGHTHOUSE", resNeeded = listOf(GLASS, STONE), prevName = "CARAVANSERY", specialAction = { p, n -> p.copy(gold = p.gold + p.usedCards.count { it.cardColour == YELLOW } + 1) }, endGameSpecialPoints = { p, n -> p.usedCards.count { it.cardColour == YELLOW } }, cardColour = YELLOW),
                Card(age = 3, name = "ARENA", resNeeded = listOf(STONE, STONE, ORE), prevName = "DISPENSARY", specialAction = { p, n -> p.copy(gold = p.gold + p.wonderLevel * 3) }, endGameSpecialPoints = { p, n -> p.wonderLevel }, cardColour = YELLOW),
                Card(age = 3, name = "FORTIFICATIONS", prevName = "WALLS", resNeeded = listOf(CLAY, CLAY, CLAY, STONE), fight = 3, cardColour = RED),
                Card(age = 3, name = "ARSENAL", resNeeded = listOf(CLAY, WOOD, WOOD, LOOM), fight = 3, cardColour = RED),
                Card(age = 3, name = "SIEGE WORKSHOP", prevName = "LABORATORY", resNeeded = listOf(CLAY, CLAY, CLAY, WOOD), fight = 3, cardColour = RED),
                Card(age = 3, name = "LODGE", resNeeded = listOf(CLAY, CLAY, WOOD, PAPYRUS), guild = setOf(COMPASS), prevName = "DISPENSARY", cardColour = GREEN),
                Card(age = 3, name = "OBSERVATORY", resNeeded = listOf(ORE, ORE, GLASS, LOOM), guild = setOf(GEAR), prevName = "LABORATORY", cardColour = GREEN),
                Card(age = 3, name = "UNIVERSITY", resNeeded = listOf(WOOD, WOOD, PAPYRUS, GLASS), guild = setOf(TABLE), prevName = "LIBRARY", cardColour = GREEN),
                Card(age = 3, name = "ACADEMY", resNeeded = listOf(STONE, STONE, STONE, GLASS), guild = setOf(COMPASS), prevName = "SCHOOL", cardColour = GREEN),
                Card(age = 3, name = "SCHOOL", resNeeded = listOf(WOOD, PAPYRUS, LOOM), guild = setOf(GEAR), prevName = "SCHOOL", cardColour = GREEN)
        )

        fun cards() = age1 + age2 + age3

        fun getAllAgeCards(age: Int): Cards = when (age) {
            1 -> age1
            2 -> age2
            3 -> age3
            else -> throw Error("No such age")
        }
    }
}