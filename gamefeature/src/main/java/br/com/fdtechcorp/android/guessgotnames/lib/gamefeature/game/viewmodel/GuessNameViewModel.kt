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

    private val _gameList = MutableLiveData<List<CharacterModel>>()
    val gameList: LiveData<List<CharacterModel>> = _gameList

    private val _characterToBeGuessed = MutableLiveData<CharacterModel>()
    private val _characterNameToBeGuessed = MutableLiveData<String>()
    val characterNameToBeGuessed: LiveData<String> = _characterNameToBeGuessed

    fun initGame(gameMode: GameMode) {
        if (isFirstRun()) {
            gameSetup(gameMode)
        }
    }

    fun userGuess(character: CharacterModel) {

    }

    private fun isFirstRun() = _listOfCharacters.value == null

    private fun gameSetup(gameMode: GameMode) {
        _gameState.value = GameState.SETUP
        _toolbarConfig.value = Pair(gameMode.titleStringResId, gameMode == GameMode.TIMED_MODE)

        fetchCharactersList()
    }

    private fun restartGame() {
        _gameState.value = GameState.STARTED

        val gameList = generateRandomListForGame()
        val profileToBeGuessed = generatedRandomProfileForGame(gameList)

        _gameList.value = gameList
        _characterToBeGuessed.value = profileToBeGuessed
        _characterNameToBeGuessed.value = "${profileToBeGuessed.firstName} ${profileToBeGuessed.lastName}"
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

    private fun generateRandomListForGame(): List<CharacterModel> {
        val charactersList = _listOfCharacters.value ?: listOf()
        val charactersListSize = charactersList.size
        val minCharsToDisplay = minOf(6, charactersListSize)

        val characterIndexesSet = mutableSetOf<Int>()
        while (characterIndexesSet.size < minCharsToDisplay) {
            characterIndexesSet.add((0 until charactersListSize).random())
        }

        return characterIndexesSet.map { index ->
            charactersList[index]
        }
    }

    private fun generatedRandomProfileForGame(gameList: List<CharacterModel>): CharacterModel {
        return gameList.random()
    }
}