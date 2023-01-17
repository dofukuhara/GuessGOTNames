package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameMode
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameState

class GuessNameViewModel : ViewModel() {

    private val _toolbarConfig = MutableLiveData<Pair<Int, Boolean>>()
    val toolbarConfig: LiveData<Pair<Int, Boolean>> = _toolbarConfig

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    fun initGame(gameMode: GameMode) {
        _gameState.value = GameState.SETUP
        _toolbarConfig.value = Pair(gameMode.titleStringResId, gameMode == GameMode.TIMED_MODE)
    }
}