package br.com.fdtechcorp.android.guessgotnames.lib.common.di

import br.com.fdtechcorp.android.guessgotnames.lib.common.service.ImageLoader
import br.com.fdtechcorp.android.guessgotnames.lib.common.service.PicassoImageLoader
import org.koin.dsl.module

val commonDiModule = module {
    factory<ImageLoader> { PicassoImageLoader() }
}
