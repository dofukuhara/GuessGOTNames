package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.module

import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel.GuessNameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameFeatureModule = module {
    // GuessName ViewModel
    viewModel { GuessNameViewModel() }
}