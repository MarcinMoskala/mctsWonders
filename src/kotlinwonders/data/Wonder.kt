package kotlinwonders.data

import kotlinwonders.Cards
import pl.marcinmoskala.kotlindownders.data.Guild.*
import pl.marcinmoskala.kotlindownders.data.Res
import pl.marcinmoskala.kotlindownders.data.Res.*
import pl.marcinmoskala.kotlindownders.data.or

data class Wonder(
        val extraRes: Res? = null,
        val name: String = "",
        val levels: Cards = listOf()
) {
    override fun toString(): String = name

    companion object {
        val Colosus = Wonder(name = "The Colosus of Rhodes", extraRes = ORE, levels = listOf(Card(resNeeded = listOf(WOOD, WOOD), points = 3), Card(resNeeded = listOf(CLAY, CLAY, CLAY), fight = 2), Card(resNeeded = listOf(ORE, ORE, ORE, ORE), points = 7)))
        val Lighthouse = Wonder(name = "The Lighthouse of Alexandria", extraRes = GLASS, levels = listOf(Card(resNeeded = listOf(STONE, STONE), points = 3), Card(resNeeded = listOf(ORE, ORE), res = listOf(ORE or CLAY or STONE or WOOD)), Card(resNeeded = listOf(GLASS, GLASS), points = 7)))
        val TempleOfArtemis = Wonder(name = "The Temple of Artemis in Ephesus", extraRes = PAPYRUS, levels = listOf(Card(resNeeded = listOf(STONE, STONE), points = 3), Card(resNeeded = listOf(WOOD, WOOD), extraGold = 9), Card(resNeeded = listOf(PAPYRUS, PAPYRUS), points = 7)))
        val Babylon = Wonder(name = "The Hanging Gardens of Babylon", extraRes = CLAY, levels = listOf(Card(resNeeded = listOf(CLAY, CLAY), points = 3), Card(resNeeded = listOf(WOOD, WOOD, WOOD), guild = COMPASS or TABLE or GEAR), Card(resNeeded = listOf(CLAY, CLAY, CLAY, CLAY), points = 7)))
        val Giza = Wonder(name = "The Pyramids of Giza", extraRes = STONE, levels = listOf(Card(resNeeded = listOf(STONE, STONE), points = 3), Card(resNeeded = listOf(WOOD, WOOD, WOOD), points = 5), Card(resNeeded = listOf(STONE, STONE, STONE, STONE), points = 7)))

        fun values() = listOf(Colosus, Lighthouse, TempleOfArtemis, Babylon, Giza)
        val NONE = Wonder()
    }
}