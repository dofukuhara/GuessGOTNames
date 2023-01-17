package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import com.google.gson.annotations.SerializedName

data class CharacterModel(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("imageUrl") val imageUrl: String
)
