package kotlinwonders.functions.action

import kotlinwonders.BuyVariant
import kotlinwonders.Mines
import kotlinwonders.Resources
import kotlinwonders.test.assertEquals
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.data.Res
import pl.marcinmoskala.kotlindownders.data.Res.*

private val freeElem: Set<BuyVariant> = setOf(mapOf())
private val noElem: Set<BuyVariant> = setOf()

internal fun getBuyResourcesVariants(need: Resources, have: Mines, neighborsHave: Map<Int, Mines>): Set<BuyVariant> =
    innerBuyResourcesVariants(need, have.sortedBy { it.size }, neighborsHave.toListOfPair())

private fun innerBuyResourcesVariants(need: Resources, have: Mines, neighborsHave: List<Pair<Int, Set<Res>>>): Set<BuyVariant> =
        if (need.isEmpty()) freeElem
        else if (have.isEmpty()) searchNeighborVariants(need, neighborsHave)
        else have[0]
                .filter { it in need }
                .fold(innerBuyResourcesVariants(need, have.drop(1), neighborsHave)) { l, r ->
                    l + innerBuyResourcesVariants(need.minus(r), have.drop(1), neighborsHave)
                }

private fun searchNeighborVariants(need: Resources, neighborsHave: List<Pair<Int, Set<Res>>>): Set<BuyVariant> =
        if (need.isEmpty()) freeElem
        else if (!canHave(need, neighborsHave.map { it.second })) noElem
        else {
            neighborsHave.filter { need[0] in it.second }
                    .map { n -> searchNeighborVariants(need.drop(1), neighborsHave - n).map { it.mapAdd(mapToList(n, need[0])) }.toSet() }
                    .foldRight(noElem) { l: Set<BuyVariant>, r: Set<BuyVariant> -> (l + r).toSet() }
        }

private fun Map<Int, Mines>.toListOfPair(): List<Pair<Int, Set<Res>>> = flatMap { n -> n.value.map { n.key to it } }

private fun <T> mapToList(n: Pair<Int, Set<T>>, need: T) = mapOf(n.first to listOf(need))

private infix fun <T> Map<Int, List<T>>.mapAdd(otherMap: Map<Int, List<T>>): Map<Int, List<T>> =
        otherMap + mapValues { if(it.key in otherMap) it.value + otherMap[it.key]!! else it.value }

class GetBuyResourcesVariantsTest {

    @Test
    fun testMapAdd() {
        assertEquals(mapOf<Int, Resources>() mapAdd mapOf<Int, Resources>(), mapOf<Int, Resources>())
        assertEquals(mapOf(1 to listOf(CLAY)) mapAdd mapOf<Int, Resources>(), mapOf(1 to listOf(CLAY)))
        assertEquals(mapOf<Int, Resources>() mapAdd mapOf(1 to listOf(CLAY)), mapOf(1 to listOf(CLAY)))
        assertEquals(mapOf(1 to listOf(CLAY)) mapAdd mapOf(1 to listOf(CLAY)), mapOf(1 to listOf(CLAY, CLAY)))
        assertEquals(mapOf(1 to listOf(CLAY)) mapAdd mapOf(1 to listOf(WOOD)), mapOf(1 to listOf(CLAY, WOOD)))
        assertEquals(mapOf(1 to listOf(CLAY)) mapAdd mapOf(1 to listOf(WOOD), 2 to listOf(ORE)), mapOf(1 to listOf(CLAY, WOOD), 2 to listOf(ORE)))
        assertEquals(mapOf(1 to listOf(CLAY), 2 to listOf(ORE)) mapAdd mapOf(), mapOf(1 to listOf(CLAY), 2 to listOf(ORE)))
    }

    @Test
    fun testSearchNeighborVariants() {
        assertEquals(searchNeighborVariants(listOf(CLAY), listOf(1 to setOf(CLAY))), setOf(mapOf(1 to listOf(CLAY))))
        assertEquals(searchNeighborVariants(listOf(CLAY), listOf(1 to setOf(CLAY), 2 to setOf(CLAY))), setOf(mapOf(1 to listOf(CLAY)), mapOf(2 to listOf(CLAY))))
        assertEquals(searchNeighborVariants(listOf(CLAY), listOf(1 to setOf(CLAY), 1 to setOf(ORE), 2 to setOf(CLAY))), setOf(mapOf(1 to listOf(CLAY)), mapOf(2 to listOf(CLAY))))
        assertEquals(searchNeighborVariants(listOf(CLAY, ORE), listOf(1 to setOf(CLAY, ORE), 2 to setOf(CLAY), 1 to setOf(ORE))), setOf(mapOf(1 to listOf(ORE, CLAY)), mapOf(1 to listOf(ORE), 2 to listOf(CLAY))))
    }

    @Test
    fun testToListOfPairs() {
        assertEquals(mapOf(1 to listOf(setOf(CLAY))).toListOfPair(), listOf(1 to setOf(CLAY)))
    }

    @Test
    fun testEmptyBuy() {
        assertEquals(getBuyResourcesVariants(emptyList(), emptyList(), emptyMap()), freeElem)
    }

    @Test
    fun testSomeBuy() {
        assertEquals(getBuyResourcesVariants(listOf(CLAY), emptyList(), mapOf(1 to listOf(setOf(CLAY)))), setOf(mapOf(1 to listOf(CLAY))))
    }

    @Test
    fun testSomeBuySomeHave() {
        assertEquals(getBuyResourcesVariants(listOf(CLAY, ORE), listOf(setOf(ORE)), mapOf(1 to listOf(setOf(CLAY)))), setOf(mapOf(1 to listOf(CLAY))))
    }

    @Test
    fun testSomeHave() {
        assertEquals(getBuyResourcesVariants(listOf(ORE), listOf(setOf(ORE)), mapOf()), freeElem)
    }

    @Test
    fun testSimpleBuy() {
        val v = getBuyResourcesVariants(
                need = listOf(GLASS, ORE),
                have = listOf(setOf(GLASS)),
                neighborsHave = mapOf(1 to listOf(setOf(ORE))))
        assertEquals(v, setOf(mapOf(1 to listOf(ORE))))
    }

    @Test
    fun testTwoOptionsFromOne() {
        val v = getBuyResourcesVariants(
                need = listOf(GLASS, ORE),
                have = listOf(setOf(GLASS)),
                neighborsHave = mapOf(1 to listOf(setOf(GLASS, ORE))))
        assertEquals(v, setOf(mapOf(1 to listOf(ORE))))
    }

    @Test
    fun testMultiVariant() {
        val v = getBuyResourcesVariants(
                need = listOf(GLASS, ORE, WOOD),
                have = listOf(setOf(GLASS)),
                neighborsHave = mapOf(1 to listOf(setOf(GLASS, ORE)), 2 to listOf(setOf(WOOD, ORE))))
        assertEquals(v, setOf(mapOf(1 to listOf(ORE), 2 to listOf(WOOD))))
    }
}