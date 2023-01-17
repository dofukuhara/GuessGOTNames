package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import br.com.fdtechcorp.android.guessgotnames.lib.common.network.ModelMapper

class CharacterModelMapper : ModelMapper<CharactersListVo, List<CharacterModel>>{
    override fun transform(voData: CharactersListVo): List<CharacterModel> {
        return voData.map { vo ->
            CharacterModel(firstName = vo.firstName, lastName = vo.lastName, imageUrl = vo.imageUrl)
        }
    }
}