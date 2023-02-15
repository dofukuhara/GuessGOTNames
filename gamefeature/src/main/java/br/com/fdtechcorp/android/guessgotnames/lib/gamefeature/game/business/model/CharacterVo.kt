package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class CharactersListVo : ArrayList<CharacterVo>()

@Keep
data class CharacterVo(
    @SerializedName("id") val id: Long?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("family") val family: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("imageUrl") val imageUrl: String?
)
