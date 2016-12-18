package kotlinwonders.ui

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlinwonders.data.*

fun <T> openMultipleChooseWindow(title: String, options: List<T>, howMuch: Int, callback: (List<T>) -> Unit) {
    val root = Pane()
    root.children.add(getMultipleChooseView(options, howMuch, callback))
    Stage().apply {
        setTitle(title)
        scene = Scene(root, 600.0, 600.0)
    }.show()
}

private fun <T> getMultipleChooseView(options: List<T>, howMuch: Int, callback: (List<T>) -> Unit): ListView<T> {
    return ListView<T>().apply {
        items = observableListOf(options)
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        setListDisplay { item ->
            text = item.displayText()
        }
        setOnMouseClicked {
            if (selectionModel.selectedItems.size == howMuch) {
                (it.source as Node).scene.window.hide()
                callback(selectionModel.selectedItems.toList())
            }
        }
        setPrefSize(600.0, 600.0)
    }
}

private fun <T> T.displayText(): String {
    return when (this) {
        null -> ""
        is Card -> cardName(this)
        is Action -> this.display()
        else -> toString()
    }
}

fun Action.display() = when (this) {
    is BurnCardAction -> "Akcja spalenia ${cardName(card)}"
    is TakeCardAction -> "Akcja wzięcia ${cardName(card)}"
    is BuyCardAction -> "Akcja kupna ${cardName(card)}, kasa: $money"
    is BuildLevelAction -> "Akcja budowy poziomu ${cardName(card)}"
    is BuyLevelAction -> "Akcja kupna poziomu ${cardName(card)}, kasa: $money"
    else -> toString()
}

private fun cardName(item: Card) = cardTranslationsToPolish[item.name] ?: item.name

fun <T> ListView<T>.setListDisplay(function: ListCell<T>.(T) -> Unit) {
    setCellFactory {
        object : ListCell<T>() {
            override fun updateItem(item: T, empty: Boolean) {
                super.updateItem(item, empty)
                function(item)
            }
        }
    }
}

private fun <T> observableListOf(items: List<T>): ObservableList<T> {
    return FXCollections.observableArrayList<T>().apply { addAll(items) }
}