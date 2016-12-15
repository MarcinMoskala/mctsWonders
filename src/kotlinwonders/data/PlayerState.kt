package kotlinwonders.data

import kotlinwonders.Cards
import kotlinwonders.Mines
import kotlinwonders.data.*
import kotlinwonders.data.Wonder.Companion.NONE
import pl.marcinmoskala.kotlindownders.data.Res

data class PlayerState(
        val id: Int = 0,
        val usedCards: Cards = listOf(),
        val gold: Int = 0,
        val wonder: Wonder = NONE,
        val wonderLevel: Int = 0,
        val buyCost: Map<Int, Map<Res, Int>> = mapOf(),
        val fightPoints: Int = 0
) {
    val playerRes: Mines
        get() = resForNeighbors + wonderRes

    val resForNeighbors: Mines
        get() = usedCards.flatMap { it.res }

    val levels: Cards
        get() = wonder.levels.take(wonderLevel)

    val wonderRes: Mines
        get() = levels.flatMap(Card::res) + (wonder.extraRes?.let { listOf(setOf(it)) } ?: listOf())

    infix fun have(card: Card): Boolean = card in usedCards
    infix fun haveAny(card: Cards): Boolean = card.any { it in usedCards }

    val nextLevel: Card?
        get() = if (wonder.levels.size > wonderLevel) wonder.levels[wonderLevel] else null

    override fun equals(other: Any?): Boolean = if (other is PlayerState) id == other.id else false
    override fun hashCode() = id
}
