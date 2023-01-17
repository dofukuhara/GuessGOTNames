package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class CharactersListVo : ArrayList<CharacterVo>()

@Keep
data class CharacterVo(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("imageUrl") val imageUrl: String
)
