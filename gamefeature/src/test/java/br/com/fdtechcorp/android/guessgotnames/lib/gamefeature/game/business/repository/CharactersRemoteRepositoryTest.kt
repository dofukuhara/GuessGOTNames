package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.NoDataFound
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.ParseVoToModelException
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapperTestHelper as ModelMapperTestHelper

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersRemoteRepositoryTest {

    private lateinit var repository: CharactersRepository

    private val apiMock : CharactersServiceApi = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = CharactersRemoteRepository(apiMock, CharacterModelMapper())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given An Empty Vo Response From Api, When Fetch Characters From Repository, Then NoDataFound Exception Must Be Returned`() = runTest {
        // Given
        coEvery { apiMock.fetchListOfCharacters() } returns ModelMapperTestHelper.getEmptyResponse()

        // When
        val response = repository.getCharacters()

        // Then
        assertTrue(
            "When an empty Vo is returned, then A NoDataFound exception will be returned",
            (response as? Either.FAILURE)?.exception is NoDataFound
        )
        assertThat(
            "When an empty Vo is returned, then A NoDataFound exception message must state what happened",
            (response as? Either.FAILURE)?.exception?.message, `is`("Failed to retrieve list of characters from remote repository"))
    }

    @Test
    fun `given A Complete Single Item Vo Response From Api, When Fetch Characters From Repository, Then A Single Model With All Fields Must Be Returned`() = runTest {
        // Given
        coEvery { apiMock.fetchListOfCharacters() } returns ModelMapperTestHelper.getCompleteSingleItemResponse()

        // When
        val response = repository.getCharacters()

        // Then
        assertThat(
            "When a complete single item Vo is returned, then a single list model item should be generated",
            (response as? Either.SUCCESS)?.data?.size, `is`(1)
        )
    }

    @Test
    fun `given A Failure From Mapper In Producing A Model From Vo, When Fetch Characters From Repository, Then A Exception Must Be Returned As EitherFAILURE`() = runTest {
        // Given
        coEvery { apiMock.fetchListOfCharacters() } returns ModelMapperTestHelper.getMissingFirstNameResponse()

        // When
        val response = repository.getCharacters()

        // Then
        assertTrue(
            "When ModelMapper returns a ParseVoToModelException exception, then pass it forward",
            (response as? Either.FAILURE)?.exception is ParseVoToModelException
        )
        assertThat(
            "When ModelMapper returns a ParseVoToModelException exception, ensure that the message will be passed forward",
            (response as? Either.FAILURE)?.exception?.message,
            `is`("Failed to parse CharactersListVo into CharacterModel [Missing firstName field]"))
    }

    @Test
    fun `given An Unknown Error From Api, When Fetch Characters From Repository, Then Produce A NoDataFound Exception And Return It`() = runTest {
        // Given
        coEvery { apiMock.fetchListOfCharacters() } throws Throwable()

        // When
        val response = repository.getCharacters()

        // Then
        assertTrue(
            "When API produces an unknown error, wrap it in the custom NoDataFound exception",
            (response as? Either.FAILURE)?.exception is NoDataFound
        )
        assertThat(
            "When API produces an unknown error, generate a NoDataFound with the proper error message",
            (response as? Either.FAILURE)?.exception?.message,
            `is`("Failed to retrieve list of characters from remote repository"))
    }
}