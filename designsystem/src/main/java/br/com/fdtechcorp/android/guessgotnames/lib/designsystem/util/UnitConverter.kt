package br.com.fdtechcorp.android.guessgotnames.lib.designsystem.util

import android.content.Context

object UnitConverter {
    fun dpToPixel(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
