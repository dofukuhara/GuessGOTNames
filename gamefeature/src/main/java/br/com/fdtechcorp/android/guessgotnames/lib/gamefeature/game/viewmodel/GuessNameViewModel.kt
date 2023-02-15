package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.fold
import br.com.fdtechcorp.android.guessgotnames.lib.common.timer.TimerCase
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.*
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuessNameViewModel(
    private val backgroundDispatcher: CoroutineDispatcher,
    private val repository: CharactersRepository,
    private val timer: TimerCase,
    private val gameConfig: GameConfig
) : ViewModel() {

    // UI - Toolbar Configuration
    private val _toolbarConfig = MutableLiveData<Pair<Int, Boolean>>()
    val toolbarConfig: LiveData<Pair<Int, Boolean>> = _toolbarConfig

    // UI - Game State Configuration
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    // UI - List of Characters to display in the grid
    private val _gameList = MutableLiveData<List<CharacterModel>>()
    val gameList: LiveData<List<CharacterModel>> = _gameList

    // UI - Name (String) of the character to be guessed
    private val _characterNameToBeGuessed = MutableLiveData<String>()
    val characterNameToBeGuessed: LiveData<String> = _characterNameToBeGuessed

    // UI - Timer percentage (for Timed Mode)
    private val _timerPercentage = MutableLiveData<Int>()
    val timerPercentage: LiveData<Int> = _timerPercentage

    // ViewModel data - full list of characters retrieved from repository
    private val _listOfCharacters = MutableLiveData<List<CharacterModel>>()

    // ViewModel data - Character model to be guessed
    private val _characterToBeGuessed = MutableLiveData<CharacterModel>()

    // ViewModel data - Game score counter
    private val _scoreCount = MutableLiveData(0)

    // ViewModel data - selected Game Mode (Practice/Timed)
    private val _gameMode = MutableLiveData<GameMode>()

    // ViewModel data - state variable to enable/disable avatar card click handling
    private val _shouldBlockClick = MutableLiveData(false)

    // ViewModel data - handling one-time initialization of the Timer
    private val _wasTimerInitialized = MutableLiveData(false)

    fun initGame(gameMode: GameMode) {
        if (isFirstRun()) {
            gameSetup(gameMode)
        }
    }

    private fun isFirstRun() = _listOfCharacters.value == null

    private fun gameSetup(gameMode: GameMode) {
        _gameState.value = GameState.SETUP
        _gameMode.value = gameMode
        _toolbarConfig.value = Pair(gameMode.titleStringResId, gameMode == GameMode.TIMED_MODE)

        fetchCharactersList()
    }

    fun userGuess(selectedCharacter: CharacterModel) {
        if (_shouldBlockClick.value == true) {
            return
        }

        val listOfChars = _gameList.value ?: listOf()
        val charToBeGuessed = _characterToBeGuessed.value
        val charSelected = listOfChars.find { it == selectedCharacter }

        if (charSelected == charToBeGuessed) {
            charSelected?.guessState = GuessState.RIGHT
            notifyViewWithRightGuess(listOfChars)
        } else {
            charSelected?.guessState = GuessState.WRONG
            notifyViewWithWrongGuess(listOfChars)
        }
    }

    private fun notifyViewWithRightGuess(listOfChars: List<CharacterModel>) {
        _gameList.value = listOfChars
        viewModelScope.launch {
            _shouldBlockClick.value = true
            delay(2000)
            restartGame()
        }
        updateUserScore()
    }

    private fun notifyViewWithWrongGuess(listOfChars: List<CharacterModel>) {
        if (_gameMode.value == GameMode.PRACTICE_MODE) {
            _shouldBlockClick.value = true
            _gameState.value = GameState.LOOSE(_scoreCount.value ?: 0)
        }
        _gameList.value = listOfChars
    }

    private fun updateUserScore() {
        val scoreCounter = _scoreCount.value ?: 0
        _scoreCount.value = scoreCounter + 1
    }

    private fun restartGame() {
        _gameState.value = GameState.STARTED
        _shouldBlockClick.value = false

        val gameList = generateRandomListForGame()
        val profileToBeGuessed = generatedRandomProfileForGame(gameList)

        _gameList.value = gameList
        _characterToBeGuessed.value = profileToBeGuessed
        _characterNameToBeGuessed.value = "${profileToBeGuessed.firstName} ${profileToBeGuessed.lastName}"

        initTimerForTimedMode()
    }

    private fun initTimerForTimedMode() {
        if (_wasTimerInitialized.value == false && _gameMode.value == GameMode.TIMED_MODE) {
            _wasTimerInitialized.value = true

            timer.timerFlow.toggleTime(gameConfig.timerForTimedModeInSeconds, viewModelScope)
            viewModelScope.launch {
                timer.timerStateFlow.collect { timerState ->
                    _timerPercentage.value = timerState.progressPercentage
                    if (timerState.secondsRemaining == 0) {
                        _gameState.value = GameState.LOOSE(_scoreCount.value ?: 0)
                    }
                }
            }
        }
    }

    private fun fetchCharactersList() {
        _gameState.value = GameState.DATAFETCH
        viewModelScope.launch {
            val repositoryResult = withContext(backgroundDispatcher) {
                repository.getCharacters()
            }

            repositoryResult.fold(
                failureHandler = {
                    _gameState.value = GameState.FAILURE
                }, successHandler = { listOfCharacterModel ->
                    if (listOfCharacterModel.isEmpty()) {
                        _gameState.value = GameState.FAILURE
                    } else {
                        _listOfCharacters.value = listOfCharacterModel
                        restartGame()
                    }
                })
        }
    }

    private fun generateRandomListForGame(): List<CharacterModel> {
        val charactersList = _listOfCharacters.value ?: listOf()
        val charactersListSize = charactersList.size
        val minCharsToDisplay = minOf(gameConfig.numberOfProfiles, charactersListSize)

        val characterIndexesSet = mutableSetOf<Int>()
        while (characterIndexesSet.size < minCharsToDisplay) {
            characterIndexesSet.add((0 until charactersListSize).random())
        }

        return characterIndexesSet.map { index ->
            charactersList[index].copy() // Make a copy of the Char Item, so that we don't modify the original one
        }
    }

    private fun generatedRandomProfileForGame(gameList: List<CharacterModel>): CharacterModel {
        return gameList.random()
    }
}