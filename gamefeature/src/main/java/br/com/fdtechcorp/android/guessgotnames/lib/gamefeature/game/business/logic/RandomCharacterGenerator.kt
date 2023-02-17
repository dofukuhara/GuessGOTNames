package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.logic

import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameConfig

class RandomCharacterGenerator {
    fun randomizeAndPickOneCharacter(gameConfig: GameConfig, list: List<CharacterModel>?) : RandomCharacterGeneratorResult? {
        val charactersList = if (list.isNullOrEmpty()) return null else list
        val charactersListSize = charactersList.size
        val minCharsToDisplay = minOf(gameConfig.numberOfProfiles, charactersListSize)

        val characterIndexesSet = mutableSetOf<Int>()
        while (characterIndexesSet.size < minCharsToDisplay) {
            characterIndexesSet.add((0 until charactersListSize).random())
        }

        val randomList = characterIndexesSet.map { index ->
            charactersList[index].copy() // Make a copy of the Char Item, so that we don't modify the original one
        }
        val characterToBeGuessed = randomList.random()

        return RandomCharacterGeneratorResult(randomList, characterToBeGuessed)
    }
}

data class RandomCharacterGeneratorResult(val randomList: List<CharacterModel>, val characterToBeGuessed: CharacterModel)