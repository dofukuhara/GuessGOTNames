package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.logic

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapper
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapperTestHelper
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameConfig
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RandomCharacterGeneratorTest {

    private lateinit var randomizer: RandomCharacterGenerator

    @Before
    fun setUp() {
        randomizer = RandomCharacterGenerator()
    }

    @Test
    fun `given An Empty List To Randomizer, When Performing The Randomization And Char Selection, Then Return Null`() {
        // Given
        val emptyList = emptyList<CharacterModel>()
        val gameConfig = GameConfig(6, 1200)

        // When
        val randomResult = randomizer.randomizeAndPickOneCharacter(gameConfig, emptyList)

        // Then
        assertNull(
            "Given an empty list, then randomizeAndPickOneCharacter must return null",
            randomResult
        )
    }

    @Test
    fun `given A Configuration Of 6 Chars But List Contain Only One, When Performing The Randomization And Char Selection, Then Return This Single Item`() {
        // Given
        val gameConfig = GameConfig(6, 1200)
        val model = (CharacterModelMapper().transform(CharacterModelMapperTestHelper.getCompleteSingleItemResponse()) as Either.SUCCESS).data

        // When
        val randomResult = randomizer.randomizeAndPickOneCharacter(gameConfig, model)

        // Then
        assertThat(
            "In case that the provided list has less values then game profile settings, then return all elements from the provided list",
            randomResult?.randomList?.size,
            `is`(1)
        )
        assertThat(
            "In case of a single item, return this item as the character to be guessed",
            randomResult?.characterToBeGuessed,
            `is`(model[0])
        )
    }

    @Test
    fun `given A Configuration Of 6 Chars But List Contain 10, When Performing The Randomization And Char Selection, Then Return Only 6 Characters`() {
        // Given
        val gameConfig = GameConfig(6, 1200)
        val model = (CharacterModelMapper().transform(CharacterModelMapperTestHelper.getCompleteTenItemsResponse()) as Either.SUCCESS).data

        // When
        val randomResult = randomizer.randomizeAndPickOneCharacter(gameConfig, model)

        // Then
        assertThat(
            "The provided list contains more than six elements.",
            model.size,
            `is`(10)
        )
        assertThat(
            "But the result randomized list size must be as same as per game configuration setting.",
            randomResult?.randomList?.size,
            `is`(6)
        )
    }
}
