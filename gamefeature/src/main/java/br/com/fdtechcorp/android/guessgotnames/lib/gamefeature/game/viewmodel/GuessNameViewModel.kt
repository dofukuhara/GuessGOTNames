package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameMode
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameState
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuessNameViewModel(
    private val backgroundDispatcher: CoroutineDispatcher,
    private val repository: CharactersRepository
) : ViewModel() {

    private val _toolbarConfig = MutableLiveData<Pair<Int, Boolean>>()
    val toolbarConfig: LiveData<Pair<Int, Boolean>> = _toolbarConfig

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    private val _listOfCharacters = MutableLiveData<List<CharacterModel>>()

    fun initGame(gameMode: GameMode) {
        if (isFirstRun()) {
            gameSetup(gameMode)
        }
    }

    private fun isFirstRun() = _listOfCharacters.value == null

    private fun gameSetup(gameMode: GameMode) {
        _gameState.value = GameState.SETUP
        _toolbarConfig.value = Pair(gameMode.titleStringResId, gameMode == GameMode.TIMED_MODE)

        fetchCharactersList()
    }

    private fun restartGame() {
        _gameState.value = GameState.STARTED
    }

    private fun fetchCharactersList() {
        _gameState.value = GameState.DATAFETCH
        viewModelScope.launch {
            val repositoryResult = withContext(backgroundDispatcher) {
                repository.getCharacters()
            }

            when (repositoryResult) {
                is Either.FAILURE -> _gameState.value = GameState.FAILURE
                is Either.SUCCESS -> {
                    val charactersList = repositoryResult.data
                    if (charactersList.isEmpty()) {
                        _gameState.value = GameState.FAILURE
                    } else {
                        _listOfCharacters.value = charactersList
                        restartGame()
                    }
                }
            }
        }
    }
}