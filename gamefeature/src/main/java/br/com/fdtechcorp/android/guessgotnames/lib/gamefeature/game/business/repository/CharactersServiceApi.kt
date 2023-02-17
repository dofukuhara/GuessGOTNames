package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository

import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharactersListVo
import retrofit2.http.GET

interface CharactersServiceApi {
    @GET("Characters")
    suspend fun fetchListOfCharacters(): CharactersListVo?
}
