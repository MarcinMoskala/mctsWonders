package kotlinwonders.player.mcts

import kotlinwonders.data.Action
import kotlinwonders.data.BurnCardAction
import kotlinwonders.data.BuyCardAction
import kotlinwonders.data.BuyLevelAction

fun discreditBurnCard(action: Action?) = when (action) {
//    is BurnCardAction -> 0.95
//    is BuyLevelAction, is BuyCardAction -> 0.98
    else -> 1.0
}