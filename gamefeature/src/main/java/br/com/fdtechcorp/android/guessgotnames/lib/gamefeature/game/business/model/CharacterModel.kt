package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

data class CharacterModel(
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    var guessState: GuessState = GuessState.NOT_SET
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterModel

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}
