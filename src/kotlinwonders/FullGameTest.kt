package kotlinwonders

import kotlinwonders.data.*
import kotlinwonders.functions.getAllPossibleActionsForPlayerState
import kotlinwonders.functions.getAllPossiblePlayerCards
import kotlinwonders.functions.getNextGameState
import kotlinwonders.functions.removeUsedCards
import kotlinwonders.VisibleState
import kotlinwonders.test.assertTheSame
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson

class FullGameTest {

    private fun cardByName(name: String) = Card.age1.first { it.name == name }
    private fun cardsByName(vararg names: String) = Card.age1.filter { it.name in names }

    var gameState = GameState(listOf(
            PlayerState(id = 0, gold = 3, wonder = Wonder.Babylon),
            PlayerState(id = 1, gold = 3, wonder = Wonder.Colosus),
            PlayerState(id = 2, gold = 3, wonder = Wonder.Giza))
    )
    var knownCards = mapOf<Int, List<Card>>(0 to listOf(), 1 to listOf(), 2 to listOf())

    @Test
    fun testStartPlayerStates() {
        var possibleCards = getAllPossiblePlayerCards(0, gameState, knownCards)
        var expectedPossibleCards = Card.age1
        assertTheSame(possibleCards, expectedPossibleCards)
        knownCards += 0 to cardsByName("EAST TRADING POST", "WORKSHOP", "STOCKADE", "BATHS", "THEATER", "LOOM", "GUARD TOWER")

        var actions = getAllPossibleActionsForPlayerState(0, VisibleState(gameState, knownCards))
        assert(TakeCardAction(cardByName("GUARD TOWER")) in actions) { "Can take Guard TOWER because we have an extra resource from wonder" }
        assert(TakeCardAction(cardByName("BATHS")) !in actions) { "Cannot take a card because of lack of stone" }

        nextTurn(TakeCardAction(cardByName("GUARD TOWER")), TakeCardAction(cardByName("ORE VEIN")), TakeCardAction(cardByName("CLAY PIT")))
        possibleCards = getAllPossiblePlayerCards(0, gameState, knownCards)
        expectedPossibleCards = cardsByName("LUMBER YARD","STONE PIT","CLAY POOL","TIMBER YARD","GLASSWORKS","PRESS","ALTAR","WEST TRADING POST","MARKETPLACE","BARRACKS","APOTHECARY","SCRIPTORIUM")
        assertTheSame(possibleCards, expectedPossibleCards)
        assert(gameState.playersStates[2].gold == 2) { "Player spend gold to buy card" }

        actions = getAllPossibleActionsForPlayerState(0, VisibleState(gameState, knownCards))
        assert(BuyCardAction(cardByName("BARRACKS"), mapOf(1 to 2)) in actions) { "In $actions \nthere should be ${BuyCardAction(cardByName("BARRACKS"), mapOf(1 to 2))}. \n All actionsPlanned are $actions" }
        assert(BuyCardAction(cardByName("BARRACKS"), mapOf(2 to 2)) in actions) { "In $actions \nthere should be ${BuyCardAction(cardByName("BARRACKS"), mapOf(2 to 2))}" }
        nextTurn(TakeCardAction(cardByName("GUARD TOWER")), TakeCardAction(cardByName("ORE VEIN")), TakeCardAction(cardByName("CLAY PIT")))
    }

    private fun nextTurn(vararg actionList: TakeCardAction) {
        gameState = getNextGameState(gameState, actionList.toList())
        knownCards = knownCards.removeUsedCards(actionList.toList()).giveCardsToNextPerson(gameState.age)
    }
}