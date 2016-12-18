package kotlinwonders

import kotlinwonders.data.Action
import kotlinwonders.data.Card
import kotlinwonders.data.PlayerState
import pl.marcinmoskala.kotlindownders.data.Res

typealias Resources = List<Res>
typealias Cards = List<Card>
typealias PlayersStates = List<PlayerState>
typealias Actions = List<Action>
typealias Mines = List<Set<Res>>
typealias BuyVariant = Map<Int, Resources>