package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.failure
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.success
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.ParseVoToModelException
import br.com.fdtechcorp.android.guessgotnames.lib.common.network.ModelMapper

class CharacterModelMapper : ModelMapper<CharactersListVo, Either<ParseVoToModelException, List<CharacterModel>>> {
    override fun transform(voData: CharactersListVo): Either<ParseVoToModelException, List<CharacterModel>> {
        // Using ".map{}", as we wish to map VO to MODEL 1-to-1 and throw an error in case any element does not matches the UI exhibition criteria
        // In case that the Business Logic allows to just skip the element that does not matches the criteria, we could use ".mapNotNull{}"
        val modelData = voData.map { vo ->
            try {
                val firstName = vo.firstName ?: throw ParseVoToModelException(" [Missing firstName field]")
                val lastName = vo.lastName ?: throw ParseVoToModelException(" [Missing lastName field]")
                val imageUrl = vo.imageUrl ?: throw ParseVoToModelException(" [Missing imageUrl field]")

                CharacterModel(firstName, lastName, imageUrl)
            } catch (t: Throwable) {
                val errorMessage = (t as? ParseVoToModelException)?.message ?: ""
                return ParseVoToModelException("Failed to parse CharactersListVo into CharacterModel$errorMessage").failure()
            }
        }

        return modelData.success()
    }
}