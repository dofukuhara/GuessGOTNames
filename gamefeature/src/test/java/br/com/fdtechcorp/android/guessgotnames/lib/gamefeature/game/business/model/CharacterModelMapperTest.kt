package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.ParseVoToModelException
import br.com.fdtechcorp.android.guessgotnames.lib.common.network.ModelMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapperTestHelper as TestHelper

class CharacterModelMapperTest {

    private lateinit var modelMapper: ModelMapper<CharactersListVo, Either<ParseVoToModelException, List<CharacterModel>>>

    @Before
    fun setUp() {
        modelMapper = CharacterModelMapper()
    }

    @Test
    fun `given A Empty Vo Response, When Mapping Vo Into Model, Then An NullPointerException Will Be Raised`() {
        // Given
        val nullVo = TestHelper.getEmptyResponse()

        // When
        val exception = Assert.assertThrows(NullPointerException::class.java) {
            modelMapper.transform(nullVo)
        }

        // Then
        assertThat(
            "As per transform() signature, it does not allows a null object as a parameter",
            exception.message,
            `is`("nullVo must not be null")
        )
    }

    @Test
    fun `give A Complete 10 Items Vo Response, When Mapping Vo Into Model, Then Model List Size Must Match Vo List Size`() {
        // Given
        val completeTenItemsVo = TestHelper.getCompleteTenItemsResponse()

        // When
        val model = modelMapper.transform(completeTenItemsVo)

        // Then
        assertThat(
            "When transforming a complete Vo object, the size of model list items must match with vo list size",
            (model as? Either.SUCCESS)?.data?.size,
            `is`(10)
        )
    }

    @Test
    fun `given A Single Vo Response, When Mapping Vo Into Model, All Attributes Must Be Properly Transformed`() {
        // Given
        val completeSingleItemVo = TestHelper.getCompleteSingleItemResponse()

        // When
        val model = modelMapper.transform(completeSingleItemVo)

        // Then
        assertThat(
            "The firstName field from Model must be the same as from Vo object",
            (model as? Either.SUCCESS)?.data?.firstOrNull()?.firstName,
            `is`("Daenerys")
        )
        assertThat(
            "The lastName field from Model must be the same as from Vo object",
            (model as? Either.SUCCESS)?.data?.firstOrNull()?.lastName,
            `is`("Targaryen")
        )
        assertThat(
            "The imageUrl field from Model must be the same as from Vo object",
            (model as? Either.SUCCESS)?.data?.firstOrNull()?.imageUrl,
            `is`("https://thronesapi.com/assets/images/daenerys.jpg")
        )
    }

    @Test
    fun `given A Single Vo Response, When Mapping Vo Into Model, The Initial Guess State Must Be Properly Configured`() {
        // Given
        val completeSingleItemVo = TestHelper.getCompleteSingleItemResponse()

        // When
        val model = modelMapper.transform(completeSingleItemVo)

        // Then
        assertThat(
            "The firstName field from Model must be the same as from Vo object",
            (model as? Either.SUCCESS)?.data?.firstOrNull()?.guessState,
            `is`(GuessState.NOT_SET)
        )
    }

    @Test
    fun `given A Missing FirstName Field From Vo Response, When Mapping Vo Into Model, Then A ParseVoToModelException Must Be Properly Generated And Thrown`() {
        // Given
        val missingFirstName = TestHelper.getMissingFirstNameResponse()

        // When
        val model = modelMapper.transform(missingFirstName)

        // Then
        assertTrue(
            "When firstName field is missing from Vo, then mapper will throw a ParseVoToModelException",
            (model as? Either.FAILURE)?.exception is ParseVoToModelException)
        assertThat(
            "When firstName field is missing from Vo, then ParseVoToModelException message must inform the missing field",
            (model as? Either.FAILURE)?.exception?.message,
            `is`("Failed to parse CharactersListVo into CharacterModel [Missing firstName field]")
        )
    }

    @Test
    fun `given A Missing LastName Field From Vo Response, When Mapping Vo Into Model, Then A ParseVoToModelException Must Be Properly Generated And Thrown`() {
        // Given
        val missingLastName = TestHelper.getMissingLastNameResponse()

        // When
        val model = modelMapper.transform(missingLastName)

        // Then
        assertTrue(
            "When firstName field is missing from Vo, then mapper will throw a ParseVoToModelException",
            (model as? Either.FAILURE)?.exception is ParseVoToModelException)
        assertThat(
            "When firstName field is missing from Vo, then ParseVoToModelException message must inform the missing field",
            (model as? Either.FAILURE)?.exception?.message,
            `is`("Failed to parse CharactersListVo into CharacterModel [Missing lastName field]")
        )
    }

    @Test
    fun `given A Missing ImageUrl Field From Vo Response, When Mapping Vo Into Model, Then A ParseVoToModelException Must Be Properly Generated And Thrown`() {
        // Given
        val missingImageUrl = TestHelper.getMissingImageUrlResponse()

        // When
        val model = modelMapper.transform(missingImageUrl)

        // Then
        assertTrue(
            "When firstName field is missing from Vo, then mapper will throw a ParseVoToModelException",
            (model as? Either.FAILURE)?.exception is ParseVoToModelException)
        assertThat(
            "When firstName field is missing from Vo, then ParseVoToModelException message must inform the missing field",
            (model as? Either.FAILURE)?.exception?.message,
            `is`("Failed to parse CharactersListVo into CharacterModel [Missing imageUrl field]")
        )
    }
}