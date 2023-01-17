package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.failure
import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.success
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.NoDataFound
import br.com.fdtechcorp.android.guessgotnames.lib.common.network.ModelMapper
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharactersListVo

class CharactersRemoteRepository(
    private val api: CharactersServiceApi,
    private val mapper: ModelMapper<CharactersListVo, List<CharacterModel>>
) : CharactersRepository {
    override suspend fun getCharacters(): Either<NoDataFound, List<CharacterModel>> {
        return try {
            val charactersListVo = api.fetchListOfCharacters()
            val charactersModelList = mapper.transform(charactersListVo)
            charactersModelList.success()
        } catch (throwable: Throwable) {
            NoDataFound("Failed to retrieve list of characters from remote repository").failure()
        }
    }
}