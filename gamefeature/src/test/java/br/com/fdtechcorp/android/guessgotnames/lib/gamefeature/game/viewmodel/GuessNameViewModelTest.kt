package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.failure
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.success
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.NoDataFound
import br.com.fdtechcorp.android.guessgotnames.lib.common.timer.TimerCase
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.logic.RandomCharacterGenerator
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.logic.RandomCharacterGeneratorResult
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.*
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersRepository
import br.com.fdtechcorp.android.guessgotnames.lib.testutils.MainCoroutinesRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapperTestHelper as ModelMapperTestHelper

@OptIn(ExperimentalCoroutinesApi::class)
class GuessNameViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule(testDispatcher)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GuessNameViewModel

    private val repositoryMock: CharactersRepository = mockk(relaxed = true)
    private val randomCharacterGeneratorMock: RandomCharacterGenerator = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = GuessNameViewModel(
            backgroundDispatcher = testDispatcher,
            repository = repositoryMock,
            timer = TimerCase(testDispatcher),
            gameConfig = GameConfig(6, 120),
            randomCharacterGenerator = randomCharacterGeneratorMock
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given A Start Of Game In Practice Mode, When Initializing The Game, Then ViewModel Must Instructs View To Update Toolbar With Practice Mode Title`() = runTest {
        // Given
        val gameModeInPractice = GameMode.PRACTICE_MODE
        coEvery { repositoryMock.getCharacters() } just Awaits

        // When
        viewModel.initGame(gameModeInPractice)
        advanceUntilIdle()

        // Then
        assertThat(
            "When initializing game in Practice Mode, the R.string.gamefeature_practice_mode_label string resource must be passed to the view",
            viewModel.toolbarConfig.value?.first, `is`(R.string.gamefeature_practice_mode_label)
        )
    }

    @Test
    fun `given A Start Of Game In Practice Mode, When Initializing The Game, Then ViewModel Must Instructs View To Not Show Timer Icon In Toolbar`() = runTest {
        // Given
        val gameModeInPractice = GameMode.PRACTICE_MODE
        coEvery { repositoryMock.getCharacters() } just Awaits

        // When
        viewModel.initGame(gameModeInPractice)
        advanceUntilIdle()

        // Then
        assertThat(
            "When initializing game in Practice Mode, timer icon in toolbar must not be visible",
            viewModel.toolbarConfig.value?.second, `is`(false)
        )
    }

    @Test
    fun `given A Start Of Game In Timed Mode, When Initializing The Game, Then ViewModel Must Instructs View To Update Toolbar With Timed Mode Title`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        coEvery { repositoryMock.getCharacters() } just Awaits

        // When
        viewModel.initGame(gameModeInTimed)
        advanceUntilIdle()

        // Then
        assertThat(
            "When initializing game in Timed Mode, the R.string.gamefeature_timed_mode_label string resource must be passed to the view",
            viewModel.toolbarConfig.value?.first, `is`(R.string.gamefeature_timed_mode_label)
        )
    }

    @Test
    fun `given A Start Of Game In Timed Mode, When Initializing The Game, Then ViewModel Must Instructs View To Show Timer Icon In Toolbar`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        coEvery { repositoryMock.getCharacters() } just Awaits

        // When
        viewModel.initGame(gameModeInTimed)
        advanceUntilIdle()

        // Then
        assertThat(
            "When initializing game in Timed Mode, timer icon in toolbar must be visible",
            viewModel.toolbarConfig.value?.second, `is`(true)
        )
    }

    @Test
    fun `given A Timer Of 2 Minutes, When Starting The Timed Mode Game, Then Timer Percentage Must be At 100 Percent`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model

        // When
        viewModel.initGame(gameModeInTimed)

        advanceTimeBy(1)
        // Then
        assertThat(
            "When timed mode game is initiated, timer countdown must be at 100%",
            viewModel.timerPercentage.value, `is`(100))
        advanceUntilIdle()
    }

    @Test
    fun `given A Started Timer Of 2 Minutes, When Passing 60 Seconds, Then Timer Percentage Must be At 50 Percent`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        viewModel.initGame(gameModeInTimed)

        // When
        val passedSixtySeconds = 60000L
        advanceTimeBy(passedSixtySeconds)

        // Then
        assertThat(
            "After passing 60 seconds out of 120, timer countdown must be at 50%",
            viewModel.timerPercentage.value, `is`(50))
        advanceUntilIdle()
    }

    @Test
    fun `given A Started Timer Of 2 Minutes, When Passing 119 Seconds, Then Timer Percentage Must be At 1 Percent`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        viewModel.initGame(gameModeInTimed)

        // When
        val almostFinishedTimer = 119000L
        advanceTimeBy(almostFinishedTimer)

        // Then
        assertThat(
            "After passing 119 seconds out of 120, timer countdown must be at 1%",
            viewModel.timerPercentage.value, `is`(1))
        advanceUntilIdle()
    }

    @Test
    fun `given A Started Timer Of 2 Minutes, When Passing 120 Seconds, Then Timer Percentage Must be At 0 Percent`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        viewModel.initGame(gameModeInTimed)

        // When
        val timerThreshold = 120000L
        advanceTimeBy(timerThreshold)

        // Then
        assertThat(
            "After passing 120 seconds out of 120, timer countdown must be at 0%",
            viewModel.timerPercentage.value, `is`(0))
        advanceUntilIdle()
    }

    @Test
    fun `given A Started Timer Of 2 Minutes, When Passing 120 Seconds And No Guess Was Made, Then Finish Game With Zero Score`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model

        // When
        viewModel.initGame(gameModeInTimed)
        advanceUntilIdle()

        // Then
        val finishedGameState: GameState? = viewModel.gameState.value
        assertThat(
            "After the timer is finished and no guess was made, then finish the game with score zero",
            (finishedGameState as? GameState.LOOSE)?.score, `is`(0))
    }

    @Test
    fun `given A Timed Mode Game, When Tap Two Correct Avatars And Then Timer Expires, Then Finish Game With Score Of Two`() = runTest {
        // Given
        val gameModeInTimed = GameMode.TIMED_MODE
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteTenItemsResponse())
        val gameList = (model as Either.SUCCESS).data.mapIndexedNotNull { index, entry -> if (index > 5)  null else entry }
        val characterToBeGuessed = model.data[0]

        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), model.data)
        } returns RandomCharacterGeneratorResult(gameList, characterToBeGuessed)

        viewModel.initGame(gameModeInTimed)

        // When
        val passedSixtySeconds = 60000L
        advanceTimeBy(passedSixtySeconds)

        viewModel.userGuess(characterToBeGuessed)
        viewModel.userGuess(characterToBeGuessed)

        advanceUntilIdle()

        // Then
        val finishedGameState: GameState? = viewModel.gameState.value
        assertThat(
            "After the timer is finished and user guess correctly two times, then finish the game with score two",
            (finishedGameState as? GameState.LOOSE)?.score, `is`(2))
    }

    @Test
    fun `given A Exception Thrown By Repository, When Initializing The Game, Then Notify This Error To The View Via FAILURE GameState`() = runTest {
        // Given
        coEvery { repositoryMock.getCharacters() } returns NoDataFound("Failed To Retrieve Character List from repository").failure()

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "Given an exception thrown by repository, notify this error to the view via GameState.FAILURE state",
            viewModel.gameState.value, `is`(GameState.FAILURE)
        )
    }

    @Test
    fun `given An Empty List Response From Repository, When Initializing The Game, Then Notify This Error To The View Via FAILURE GameState`() = runTest {
        // Given
        coEvery { repositoryMock.getCharacters() } returns emptyList<CharacterModel>().success()

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "Given an empty list of characters, notify this error to the view via GameState.FAILURE state",
            viewModel.gameState.value, `is`(GameState.FAILURE)
        )
    }

    @Test
    fun `given Any Valid Character Response, When Initializing The Game, Then Notify View That The Game Is in STARTED GameState`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "Given any valid response of characters, notify view to draw the game content via GameState.STARTED state",
            viewModel.gameState.value, `is`(GameState.STARTED)
        )
    }

    @Test
    fun `given A Single Character Response, When Initializing The Game, Then The Character Name Must Be Properly Passed To The View`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteSingleItemResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), (model as Either.SUCCESS).data)
        } returns RandomCharacterGeneratorResult((model as Either.SUCCESS).data, model.data[0])

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "Given a single item, then show the name of this character in the view",
            viewModel.characterNameToBeGuessed.value, `is`("Daenerys Targaryen")
        )
    }

    @Test
    fun `given A List With Multiple Items, When Initializing The Game, Then One Of The Characters Name Must Be Properly Informed To The View`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteTenItemsResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), (model as Either.SUCCESS).data)
        } returns RandomCharacterGeneratorResult((model as Either.SUCCESS).data.mapIndexedNotNull { index, entry -> if (index > 5) null else entry }, model.data[0])

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "As the selection of the character is random, it can be one of those 10 entries",
            viewModel.characterNameToBeGuessed.value,
            anyOf(`is`("Daenerys Targaryen"), `is`("Samwell Tarly"), `is`("Jon Snow"),
                `is`("Arya Stark"), `is`("Sansa Stark"), `is`("Brandon Stark"))
        )
    }

    @Test
    fun `given A List With More Than 6 Items, When Initializing The Game, Then Pass A List Of 6 Characters To The View`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteTenItemsResponse())
        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), (model as Either.SUCCESS).data)
        } returns RandomCharacterGeneratorResult((model as Either.SUCCESS).data.mapIndexedNotNull { index, entry -> if (index > 5) null else entry }, model.data[0])

        // When
        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        // Then
        assertThat(
            "Given a repository result of 10 characters, viewModel must filter, randomize and return a list of 6 entries to the view",
            viewModel.gameList.value?.size, `is`(6)
        )
    }

    @Test
    fun `given A Game Already Set, When User Tap On The Correct Avatar, Then Its GuessState Value Must Be Changed To Right`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteTenItemsResponse())
        val gameList = (model as Either.SUCCESS).data.mapIndexedNotNull { index, entry -> if (index > 5)  null else entry }
        val characterToBeGuessed = model.data[0]

        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), model.data)
        } returns RandomCharacterGeneratorResult(gameList, characterToBeGuessed)

        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        assertThat(
            "Ensure that before user tap any avatar the 'to-be-guessed' character GuessState is set as NOT_SET",
            viewModel.gameList.value?.get(0)?.guessState, `is`(GuessState.NOT_SET)
        )

        // When
        viewModel.userGuess(characterToBeGuessed)

        // Then
        assertThat(
            "When user tap in the correct character avatar, its GuessState attribute must be changed to RIGHT",
            viewModel.gameList.value?.get(0)?.guessState, `is`(GuessState.RIGHT)
        )
    }

    @Test
    fun `given A Game Already Set, When User Tap On The Wrong Avatar, Then Its GuessState Value Must Be Changed To Wrong`() = runTest {
        // Given
        val model = CharacterModelMapper().transform(ModelMapperTestHelper.getCompleteTenItemsResponse())
        val gameList = (model as Either.SUCCESS).data.mapIndexedNotNull { index, entry -> if (index > 5)  null else entry }
        val characterToBeGuessed = model.data[0]
        val wrongCharacter = model.data[1]

        coEvery { repositoryMock.getCharacters() } returns model
        every {
            randomCharacterGeneratorMock.randomizeAndPickOneCharacter(any(), model.data)
        } returns RandomCharacterGeneratorResult(gameList, characterToBeGuessed)

        viewModel.initGame(GameMode.PRACTICE_MODE)
        advanceUntilIdle()

        assertThat(
            "Ensure that before user tap any avatar the 'to-be-guessed' character GuessState is set as NOT_SET",
            viewModel.gameList.value?.get(0)?.guessState, `is`(GuessState.NOT_SET)
        )
        assertThat(
            "Ensure that before user tap any avatar the 'wrong' character GuessState is set as NOT_SET",
            viewModel.gameList.value?.get(1)?.guessState, `is`(GuessState.NOT_SET)
        )

        // When
        viewModel.userGuess(wrongCharacter)

        // Then
        assertThat(
            "As the user didn't tap the correct avatar, it must remain as NOT_SET",
            viewModel.gameList.value?.get(0)?.guessState, `is`(GuessState.NOT_SET)
        )
        assertThat(
            "When user tap in the wrong character avatar, its GuessState attribute must be changed to WRONG",
            viewModel.gameList.value?.get(1)?.guessState, `is`(GuessState.WRONG)
        )
    }
}