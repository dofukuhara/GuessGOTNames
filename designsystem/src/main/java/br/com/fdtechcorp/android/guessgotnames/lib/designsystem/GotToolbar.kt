package br.com.fdtechcorp.android.guessgotnames.lib.designsystem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import br.com.fdtechcorp.android.guessgotnames.lib.designsystem.util.UnitConverter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.CircularProgressIndicator

class GotToolbar : MaterialToolbar {
    constructor(context: Context) : super(context, null) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private var progressIndicator: CircularProgressIndicator? = null

    private fun init(context: Context) {
        inflateMenu(R.menu.got_toolbar_menu)
        navigationIconConfiguration(context)
        titleColorConfiguration(context)
        backgroundColorConfiguration(context)
        paddingConfiguration(context)
        menuConfiguration(context)
    }

    private fun navigationIconConfiguration(context: Context) {
        setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        setNavigationIconTint(ContextCompat.getColor(context, R.color.got_toolbar_back_icon))
    }

    private fun titleColorConfiguration(context: Context) {
        setTitleTextColor(ContextCompat.getColor(context, R.color.got_toolbar_title))
    }

    private fun backgroundColorConfiguration(context: Context) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.got_toolbar_background))
    }

    private fun paddingConfiguration(context: Context) {
        val paddingInPixels = UnitConverter.dpToPixel(context, TOOLBAR_RIGHT_PADDING_IN_DP)
        setPadding(0, 0, paddingInPixels, 0)
    }

    private fun menuConfiguration(context: Context) {
        val menuItem = menu.findItem(R.id.got_toolbar_item_cpi)
        with(menuItem.actionView as CircularProgressIndicator) {
            progressIndicator = this
            isIndeterminate = false
            setIndicatorColor(ContextCompat.getColor(context, R.color.got_toolbar_track))
            progress = PROGRESS_INDICATOR_INITIAL_VALUE
            trackColor = ContextCompat.getColor(context, R.color.got_toolbar_track_background)
            indicatorSize = UnitConverter.dpToPixel(context, TOOLBAR_ICON_SIZE_IN_DP)
            trackThickness = UnitConverter.dpToPixel(context, PROGRESS_INDICATOR_TRACK_THICKNESS_IN_DP)
        }
    }

    fun handleTimeProgressIndicator(shouldDisplay: Boolean) {
        progressIndicator?.visibility = if (shouldDisplay) { View.VISIBLE } else { View.GONE }
    }

    fun updateProgressIndicator(progress: Int) {
        if (progress in 0..100) {
            progressIndicator?.progress = progress
        }
    }

    companion object {
        private const val TOOLBAR_RIGHT_PADDING_IN_DP = 16
        private const val PROGRESS_INDICATOR_INITIAL_VALUE = 0
        private const val TOOLBAR_ICON_SIZE_IN_DP = 24
        private const val PROGRESS_INDICATOR_TRACK_THICKNESS_IN_DP = 3
    }
}
