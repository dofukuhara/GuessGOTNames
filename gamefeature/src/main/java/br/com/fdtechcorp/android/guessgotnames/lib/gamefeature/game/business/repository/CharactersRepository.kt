package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository

import br.com.fdtechcorp.android.guessgotnames.lib.common.arch.Either
import br.com.fdtechcorp.android.guessgotnames.lib.common.exception.NoDataFound
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel

interface CharactersRepository {
    suspend fun getCharacters() : Either<NoDataFound, List<CharacterModel>>
}