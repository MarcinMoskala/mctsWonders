package pl.marcinmoskala.kotlindownders.functions

import kotlinwonders.data.Card
import kotlinwonders.data.PlayerState
import kotlinwonders.data.Wonder
import kotlinwonders.data.getNeighbors
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.data.CardColour.BROWN
import pl.marcinmoskala.kotlindownders.data.Guild
import pl.marcinmoskala.kotlindownders.data.Guild.*

fun PlayerState.getGoldPoints(): Int = gold / 3

fun PlayerState.getWonderPoints(neighbors: List<PlayerState> = listOf()): Int =
        wonderlevels.map { it.points + (it.endGameSpecialPoints?.invoke(this, neighbors) ?: 0) }
                .sum()

fun PlayerState.getCardPoints(neighbors: List<PlayerState> = emptyList()): Int =
        usedCards.map { it.points + (it.endGameSpecialPoints?.invoke(this, neighbors) ?: 0) }
                .sum()

fun PlayerState.getGreenPoints(): Int =
        allGuildUsageVariants (usedCards.mapNotNull { it.guild })
                .map { it.countGuildPoints() }
                .max() ?: 0

fun List<PlayerState>.giveFightPoints(age: Int): List<PlayerState> =
        map { p -> p.copy(fightPoints = p.fightPoints + getNeighbors(p, this).sumBy { n -> getNewFightPoints(p, n, age) }) }

data class PlayerPoints(
    val goldPoints: Int,
    val wonderPoints: Int,
    val cardPoints: Int,
    val greenPoints: Int,
    val fightPoints: Int
)

fun PlayerState.countPoints(neighbors: List<PlayerState>): PlayerPoints =
        PlayerPoints(getGoldPoints(), getWonderPoints(neighbors), getCardPoints(neighbors), getGreenPoints(), fightPoints)

fun PlayerPoints.countPoints(): Int = goldPoints + wonderPoints + cardPoints + greenPoints + fightPoints

private val PlayerState.wonderlevels: List<kotlinwonders.data.Card>
    get() = (1..wonderLevel)
            .map { levels[it - 1] }

fun allGuildUsageVariants(cards: List<Set<Guild>>): List<List<Guild>> =
        if (cards.isEmpty())
            listOf(emptyList())
        else
            cards.first()
                    .map { c -> allGuildUsageVariants(cards.filterIndexed { i, c -> i != 0 }).map { it + c } }
                    .fold(listOf(), { l1, l2 -> l1 + l2 })

private fun List<Guild>.countGuildPoints(): Int {
    val numbersOf = Guild.values().map { g -> this.filter { it == g }.count() }
    if (numbersOf.isEmpty())
        return 0
    else
        return numbersOf.map { it * it }.sum() + numbersOf.min()!! * 7
}

fun getNewFightPoints(p: PlayerState, n: PlayerState, age: Int): Int =
        when {
            p.fights < n.fights -> -1
            p.fights > n.fights ->
                when (age) {
                    1 -> 1
                    2 -> 3
                    3 -> 5
                    else -> throw Error("No such Age")
                }
            else -> 0
        }

private val PlayerState.fights: Int
    get() = usedCards.sumBy { it.fight }

fun countFinalPoints(playersStates: List<PlayerState>): List<Int> =
        playersStates.map { p -> p.countPoints(getNeighbors(p, playersStates)).countPoints() }

class PointsCountFuncsKtTest {

    @Test
    fun testGetGoldPoints() {
        val goldAndExpectedPoints = listOf(
                0 to 0,
                1 to 0,
                2 to 0,
                3 to 1,
                4 to 1,
                5 to 1,
                6 to 2,
                7 to 2,
                8 to 2,
                9 to 3,
                10 to 3)
        for ((gold, expectedPoints) in goldAndExpectedPoints) {
            assertEquals(expectedPoints, PlayerState(gold = gold).getGoldPoints())
        }
        assertEquals(0, PlayerState().getGoldPoints())
    }

    @Test
    fun testGetWonderPoints() {
        assertEquals(8, PlayerState(wonder = Wonder.Giza, wonderLevel = 2).getWonderPoints())
        assertEquals(10, PlayerState(wonder = Wonder.Colosus, wonderLevel = 3).getWonderPoints())
        assertEquals(3, PlayerState(wonder = Wonder.Lighthouse, wonderLevel = 1).getWonderPoints())
        for (w in Wonder.values()) {
            assertEquals(0, PlayerState(wonder = w, wonderLevel = 0).getWonderPoints())
        }
    }

    @Test
    fun testGetBluePoints() {
        fun playerWithCards(vararg cards: kotlinwonders.data.Card) = PlayerState(usedCards = cards.toList())
        assertEquals(0, PlayerState().getCardPoints())
        assertEquals(3, playerWithCards(Card(points = 3)).getCardPoints())
        assertEquals(5, playerWithCards(Card(points = 3), Card(points = 2)).getCardPoints())
        assertEquals(7, playerWithCards(Card(points = 3), Card(points = 2), Card(points = 2)).getCardPoints())
    }

    @Test
    fun testGetBluePointsEndSpecialPoints() {
        fun playerWithCards(vararg cards: kotlinwonders.data.Card) = PlayerState(usedCards = cards.toList())
        assertEquals(1, playerWithCards(Card(endGameSpecialPoints = { p, n -> 1 })).getCardPoints())
        assertEquals(1, playerWithCards(Card(endGameSpecialPoints = { p, n -> p.usedCards.count { it.cardColour == BROWN } }, cardColour = BROWN)).getCardPoints())
        assertEquals(1, playerWithCards(Card(endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.count { it.cardColour == BROWN } })).getCardPoints(listOf(PlayerState(usedCards = listOf(Card(cardColour = BROWN))))))
        assertEquals(3, playerWithCards(Card(points = 1, cardColour = BROWN), Card(endGameSpecialPoints = { p, n -> p.usedCards.count { it.cardColour == BROWN } }, cardColour = BROWN)).getCardPoints())
        assertEquals(2, playerWithCards(Card(points = 1), Card(endGameSpecialPoints = { p, n -> n.flatMap { it.usedCards }.count { it.cardColour == BROWN } })).getCardPoints(listOf(PlayerState(usedCards = listOf(Card(cardColour = BROWN))))))
    }

    @Test
    fun testAllGuildUsageVariants() {
        assertEquals(1, allGuildUsageVariants(listOf(setOf(GEAR))).size)
        assertEquals(1, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR))).size)
        assertEquals(1, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR), setOf(GEAR))).size)
        assertEquals(2, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR, COMPASS))).size)
        assertEquals(3, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR, COMPASS, TABLE))).size)
        assertEquals(4, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR, COMPASS), setOf(GEAR, COMPASS))).size)
        assertEquals(6, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR, COMPASS), setOf(GEAR, COMPASS, TABLE))).size)
        assertEquals(9, allGuildUsageVariants(listOf(setOf(GEAR), setOf(GEAR, COMPASS, TABLE), setOf(GEAR, COMPASS, TABLE))).size)
    }

    @Test
    fun testGetGreenPoints() {
        fun getCardWith(vararg g: Guild) = kotlinwonders.data.Card(guild = g.toSet())
        fun playerWith(vararg g: Guild) = PlayerState(usedCards = g.map { getCardWith(it) })

        for (g in Guild.values()) {
            assertEquals(1, playerWith(g).getGreenPoints())
            assertEquals(4, playerWith(g, g).getGreenPoints())
            assertEquals(9, playerWith(g, g, g).getGreenPoints())
            assertEquals(16, playerWith(g, g, g, g).getGreenPoints())
            assertEquals(25, playerWith(g, g, g, g, g).getGreenPoints())
        }

        for (g1 in Guild.values())
            for (g2 in Guild.values().filter { it != g1 }) {
                assertEquals(2, playerWith(g1, g2).getGreenPoints())
                assertEquals(5, playerWith(g1, g1, g2).getGreenPoints())
                assertEquals(8, playerWith(g1, g1, g2, g2).getGreenPoints())
                assertEquals(10, playerWith(g1, g2, g2, g2).getGreenPoints())
            }

        for (g1 in Guild.values())
            for (g2 in Guild.values().filter { it != g1 })
                for (g3 in Guild.values().filter { it != g1 && it != g2 }) {
                    assertEquals(10, playerWith(g1, g2, g3).getGreenPoints())
                    assertEquals(13, playerWith(g1, g1, g1, g2, g2).getGreenPoints())
                    assertEquals(21, playerWith(g1, g1, g1, g2, g2, g3).getGreenPoints())
                    assertEquals(12 + 14, playerWith(g1, g1, g2, g2, g3, g3).getGreenPoints())
                    assertEquals(3 * 9 + 3 * 7, playerWith(g1, g1, g1, g2, g2, g2, g3, g3, g3).getGreenPoints())
                    assertEquals(3 * 16 + 4 * 7, playerWith(g1, g1, g1, g1, g2, g2, g2, g2, g3, g3, g3, g3).getGreenPoints())
                }

        val anyGuild = getCardWith(TABLE, COMPASS, GEAR)
        val gearGuild = getCardWith(GEAR)
        val tableGuild = getCardWith(TABLE)
        assertEquals(10, PlayerState(usedCards = listOf(gearGuild, tableGuild, anyGuild)).getGreenPoints())
        assertEquals(26, PlayerState(usedCards = listOf(gearGuild, gearGuild, tableGuild, anyGuild, anyGuild, anyGuild)).getGreenPoints())
        assertEquals(37, PlayerState(usedCards = listOf(gearGuild, gearGuild, tableGuild, anyGuild, anyGuild, anyGuild, anyGuild)).getGreenPoints())
    }

    @Test
    fun testGetNewFightPoints() {
        fun playerWith(fight: Int) = PlayerState(usedCards = listOf(Card(fight = fight)))

        assertEquals(-1, getNewFightPoints(playerWith(0), playerWith(4), 1))
        assertEquals(1, getNewFightPoints(playerWith(6), playerWith(4), 1))
        assertEquals(-1, getNewFightPoints(playerWith(3), playerWith(5), 2))
        assertEquals(3, getNewFightPoints(playerWith(3), playerWith(0), 2))
        assertEquals(-1, getNewFightPoints(playerWith(4), playerWith(5), 3))
        assertEquals(5, getNewFightPoints(playerWith(3), playerWith(1), 3))
    }

    @Test
    fun testGiveFightPoints() {
        fun playersWith(vararg fight: Int) = fight.toList().map { f -> PlayerState(id = f, usedCards = listOf(Card(fight = f))) }

        val players = playersWith(0, 1, 2)
        assertEquals(listOf(-2, 0, 2), players.giveFightPoints(1).map { it.fightPoints })
        assertEquals(listOf(-2, 2, 6), players.giveFightPoints(2).map { it.fightPoints })
        assertEquals(listOf(-2, 4, 10), players.giveFightPoints(3).map { it.fightPoints })
        assertEquals(listOf(-4, 2, 8), players.giveFightPoints(1).giveFightPoints(2).map { it.fightPoints })
        assertEquals(listOf(-6, 6, 18), players.giveFightPoints(1).giveFightPoints(2).giveFightPoints(3).map { it.fightPoints })
    }

    @Test
    fun testCountPoints() {
    }
}