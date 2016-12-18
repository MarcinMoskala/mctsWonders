package kotlinwonders.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import kotlinwonders.VisibleState
import kotlinwonders.data.Action
import kotlinwonders.data.Card
import kotlinwonders.data.GameState
import kotlinwonders.data.getStartGameState
import kotlinwonders.functions.getAllPossibleActionsForPlayerState
import kotlinwonders.functions.getAllPossiblePlayerCards
import kotlinwonders.functions.getNextGameState
import kotlinwonders.functions.removeUsedCards
import kotlinwonders.player.mcts.MctsPlayer
import kotlinwonders.player.mcts.toNonIndexedList
import pl.marcinmoskala.kotlindownders.functions.giveCardsToNextPerson
import kotlin.concurrent.thread

class ChooseMultipleView : Application() {

    val allPlayers = (0..2).toList()
    val mctsPlayer = MctsPlayer(1)

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        showNothing()
        playNextRound(getStartGameState(3), getIndexedEmptyCards(3), 0)
    }

    private fun getIndexedEmptyCards(i: Int): Map<Int, List<Card>> = (0..(i - 1)).map { it to emptyList<Card>() }.toMap()

    private fun playNextRound(gameState: GameState, knownCards: Map<Int, List<Card>>, playerId: Int) {
        askForCards(playerId, gameState, knownCards) { knownCards ->
            chooseAction(playerId, gameState, knownCards) { action ->
                askForOtherPlayersActions(gameState, knownCards, action) { actions ->
                    val actionList = actions.toNonIndexedList()
                    playNextRound(
                            gameState = getNextGameState(gameState, actionList),
                            knownCards = knownCards.removeUsedCards(actionList).giveCardsToNextPerson(gameState.age),
                            playerId = playerId)
                }
            }
        }
    }

    private fun chooseAction(playerId: Int, gameState: GameState, knownCards: Map<Int, List<Card>>, callback: (Map<Int, Action>) -> Unit) {
        var stop = false
        showButton("Kiedy skończyć obliczenia?", "Teraz", { stop = true })
        startActionCalculation(gameState, knownCards, playerId, { stop }) { action ->
            showButton("Wybrana akcja to $action", "Ok") {
                callback(mapOf(playerId to action))
            }
        }
    }

    private fun startActionCalculation(gameState: GameState, knownCards: Map<Int, List<Card>>, playerId: Int, endCalcFun: (Int)->Boolean, actionCallback: (Action)->Unit) {
        thread {
            val action = mctsPlayer.startMcts(gameState, knownCards, playerId, endCalcFun)
            Platform.runLater { actionCallback(action) }
        }
    }

    private fun askForOtherPlayersActions(gameState: GameState, knownCards: Map<Int, List<Card>>, actions: Map<Int, Action>, callback: (Map<Int, Action>) -> Unit) {
        val undecidedPlayers = allPlayers.filter { it !in actions }
        if (undecidedPlayers.isEmpty())
            callback(actions)
        else {
            val playerId = undecidedPlayers.first()
            val options = getAllPossibleActionsForPlayerState(playerId, VisibleState(gameState, knownCards)).distinct()
            askPlayerForAction(playerId, options) { action ->
                askForOtherPlayersActions(gameState, knownCards, actions + (playerId to action), callback)
            }
        }
    }

    private fun askPlayerForAction(playerId: Int, options: List<Action>, callback: (Action) -> Unit) {
        openMultipleChooseWindow("Co zrobił gracz nr $playerId?", options, 1) { callback(it[0]) }
    }

    private fun askForCards(playerId: Int, gameState: GameState, knownCards: Map<Int, List<Card>>, callback: (Map<Int, List<Card>>) -> Unit) {
        val knownCardsNum = knownCards[playerId]?.size ?: 0
        val cardsNeedToHave = gameState.cardsOnHands
        if(knownCardsNum == cardsNeedToHave)
            callback(knownCards)
        else {
            val options = getAllPossiblePlayerCards(playerId, gameState, knownCards)
            openMultipleChooseWindow("Jakie masz karty?", options, gameState.cardsOnHands) {
                callback(knownCards + (playerId to it))
            }
        }

    }
}

fun <E> List<E>?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun main(args: Array<String>) {
    Application.launch(ChooseMultipleView::class.java, *args)
}