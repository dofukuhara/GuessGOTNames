package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

sealed class GameState {
    object SETUP : GameState()
    object DATAFETCH : GameState()
    object FAILURE : GameState()
    object STARTED : GameState()
    object WON : GameState()
    data class LOOSE(val score: Int) : GameState()
}
