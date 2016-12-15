package kotlinwonders.functions.action

import kotlinwonders.data.*

fun PlayerState.applyAction(action: Action, neighbors: List<PlayerState>): PlayerState = when (action) {
    is TakeCardAction -> copy(
            gold = gold + action.card.extraGold - action.card.costGold,
            usedCards = usedCards + action.card).run { action.card.specialAction?.invoke(this, neighbors) ?: this }
    is BuyCardAction -> copy(
            gold = gold + action.card.extraGold - action.card.costGold - action.cost,
            usedCards = usedCards + action.card).run { action.card.specialAction?.invoke(this, neighbors) ?: this }
    is BuildLevelAction -> copy(
            gold = gold + nextLevel!!.extraGold - nextLevel!!.costGold,
            wonderLevel = wonderLevel + 1)
            .run { action.card.specialAction?.invoke(this, neighbors) ?: this }
    is BuyLevelAction -> copy(
            gold = gold + nextLevel!!.extraGold - nextLevel!!.costGold - action.cost,
            wonderLevel = wonderLevel + 1)
            .run { action.card.specialAction?.invoke(this, neighbors) ?: this }
    is BurnCardAction -> copy(gold = gold + 3)
    else -> throw Exception("Unsupported action")
}