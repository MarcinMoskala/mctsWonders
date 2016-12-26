package kotlinwonders.player.mcts

import kotlinwonders.VisibleState
import kotlinwonders.data.*
import kotlinwonders.functions.action.getOptimalPlayerActions
import kotlinwonders.functions.getAllPossiblePlayerCards
import kotlinwonders.functions.getNextGameState
import kotlinwonders.functions.makeActionsAndThenSimulateRandomGame
import kotlinwonders.getRandomPlayers
import kotlinwonders.player.Player
import kotlinwonders.test.assertEquals
import kotlinwonders.test.cardsByName
import org.testng.annotations.Test
import pl.marcinmoskala.kotlindownders.data.Res
import pl.marcinmoskala.kotlindownders.functions.countFinalPoints
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson
import pl.marcinmoskala.kotlindownders.utills.biggestPlace

fun Leaf.simulated(simulationsPerBranch: Int, players: List<Player>): DecisionTree {
    if (visibleState.gameState.isFinal && actionsPlanned.keys.size == players.size) {
        val newGameResults = getGameResult(actionsPlanned, visibleState, simulationsPerBranch, players)
        return copy(gameResults = listOf(gameResults, newGameResults).sumLists())
    } else {
        val nextPlayerId = getNextPlayerId(actionsPlanned)
        return BranchDecisionTree(
                actionsPlanned = actionsPlanned,
                visibleState = visibleState,
                gameResults = gameResults,
                kids = getAllOptimalActionsForPlayerState(nextPlayerId, visibleState)
                        .map { newAction ->
                            val newPlannedActions = actionsPlanned + (nextPlayerId to newAction)
                            newAction to createLeaf(visibleState, newPlannedActions, simulationsPerBranch, players)
                        }.toMap()
        )
    }
}

private fun getAllOptimalActionsForPlayerState(id: Int, visibleState: VisibleState) =
        getOptimalPlayerActions(getAllPossiblePlayerCards(id, visibleState.gameState, visibleState.knownCards), visibleState.gameState.playersStates[id], getNeighbors(visibleState.gameState.playersStates[id], visibleState.gameState.playersStates))

private fun createLeaf(visibleState: VisibleState, actionsPlanned: Map<Int, Action>, simulationsPerBranch: Int, players: List<Player>): Leaf {
    val (newVisibleState, newActionsPlanned) = nextVisibleState(actionsPlanned, visibleState)
    val newGameResults = getGameResult(newActionsPlanned, newVisibleState, simulationsPerBranch, players)
    return Leaf(newVisibleState, newActionsPlanned, newGameResults)
}

private fun getGameResult(actions: Map<Int, Action>, visibleState: VisibleState, simulationsPerBranch: Int, players: List<Player>): List<Int> {
    return (1..simulationsPerBranch)
            .map { makeActionsAndThenSimulateRandomGame(visibleState, actions, players) }
            .map { countFinalPoints(it.playersStates) }
            .map(::biggestPlace)
            .sumLists()
}

private fun nextVisibleState(actions: Map<Int, Action>, visibleState: VisibleState): Pair<VisibleState, Map<Int, Action>> =
        if (visibleState.playersIds.all { it in actions.keys }) {
            val nextGameState = getNextGameState(visibleState.gameState, visibleState.playersIds.map { actions[it]!! })
            val actionsCards = actions.values.map { it.card }
            val newKnownCards = visibleState.playersIds
                    .map { visibleState.knownCards[it]?.minus(actionsCards) ?: emptyList<Card>() }
                    .giveCardsToNextPerson(visibleState.gameState.age)
                    .toIndexMap()
            VisibleState(nextGameState, newKnownCards) to mapOf()
            // !!! Wykonuje ostatnią akcję w 3,6 i puszcza make action and simulate card na pusto co wysypuje program
        } else visibleState to actions

class TestAllOptimalActions() {
    @Test fun shouldBeNoBuyLevelAction() {
        val state = VisibleState(gameState = GameState(age = 3, round = 1, playersStates = listOf(
                PlayerState(id = 0, usedCards = cardsByName("TIMBER YARD", "STOCKADE", "CLAY POOL", "BARRACKS", "LUMBER YARD", "FOUNDRY", "STATUE", "CARAVANSERY", "COURTHOUSE"), gold = 0, wonder = Wonder.Giza, wonderLevel = 3, buyCost = mapOf(), fightPoints = 4),
                PlayerState(id = 1, usedCards = cardsByName("LOOM", "GUARD TOWER", "CLAY PIT", "ORE VEIN", "STONE PIT", "LOOM", "SCHOOL", "SAWMILL", "ARCHERY RANGE", "TEMPLE"), gold = 3, wonder = Wonder.Babylon, wonderLevel = 2, buyCost = mapOf(0 to mapOf(Res.GLASS to 1, Res.LOOM to 1, Res.PAPYRUS to 1), 2 to mapOf(Res.GLASS to 1, Res.LOOM to 1, Res.PAPYRUS to 1)), fightPoints = 6),
                PlayerState(id = 2, usedCards = cardsByName("SCRIPTORIUM", "EAST TRADING POST", "APOTHECARY", "BATHS", "GLASSWORKS", "PRESS", "GLASSWORKS", "LIBRARY", "PRESS", "AQUEDUCT", "BRICKYARD"), gold = 4, wonder = Wonder.Lighthouse, wonderLevel = 0, buyCost = mapOf(0 to mapOf(Res.CLAY to 1, Res.WOOD to 1, Res.STONE to 1, Res.ORE to 1)), fightPoints = -4))
                ), knownCards = mapOf(0 to listOf(), 1 to listOf()))
        val actions = getAllOptimalActionsForPlayerState(0, state)
        assert(actions.none { it is BuildLevelAction })
    }
}