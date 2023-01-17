package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.adapter

import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel

interface CharacterClickListener {
    fun onClick(character: CharacterModel)
}