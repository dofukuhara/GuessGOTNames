package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model

import androidx.annotation.StringRes
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R

enum class GameMode(@StringRes val titleStringResId: Int) {
    PRACTICE_MODE(R.string.gamefeature_practice_mode_label),
    TIMED_MODE(R.string.gamefeature_timed_mode_label)
}