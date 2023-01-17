package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.module

import br.com.fdtechcorp.android.guessgotnames.lib.common.network.ModelMapper
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModelMapper
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharactersListVo
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersRemoteRepository
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersRepository
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.repository.CharactersServiceApi
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel.GuessNameViewModel
import br.com.fdtechcorp.android.guessgotnames.lib.network.public.RestClient
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameFeatureModule = module {
    // GuessName Repository
    factory<ModelMapper<CharactersListVo, List<CharacterModel>>> { CharacterModelMapper() }
    factory { provideCharactersServiceApi(client = get()) }
    factory<CharactersRepository> { CharactersRemoteRepository(api = get(), mapper = get()) }

    // GuessName ViewModel
    viewModel { GuessNameViewModel(
        backgroundDispatcher = Dispatchers.IO,
        repository = get()
    ) }
}

private fun provideCharactersServiceApi(client: RestClient) : CharactersServiceApi = client.getApi(CharactersServiceApi::class.java)