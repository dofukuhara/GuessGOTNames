package br.com.fdtechcorp.android.guessgotnames.lib.designsystem

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import br.com.fdtechcorp.android.guessgotnames.lib.designsystem.util.UnitConverter
import com.google.android.material.button.MaterialButton

class GotButton : MaterialButton {
    constructor(context: Context) : super(context, null) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        textConfiguration()
        backgroundConfiguration(context)
        cornerConfiguration(context)
        paddingConfiguration(context)
    }

    private fun paddingConfiguration(context: Context) {
        val paddingInPixels = UnitConverter.dpToPixel(context, BTN_VERTICAL_PADDING_IN_DP)
        setPadding(
            BTN_HORIZONTAL_PADDING_IN_PX,
            paddingInPixels,
            BTN_HORIZONTAL_PADDING_IN_PX,
            paddingInPixels
        )
    }

    private fun cornerConfiguration(context: Context) {
        val cornerRadiusInPixels = UnitConverter.dpToPixel(context, BTN_CORNER_RADIUS_IN_DP)
        cornerRadius = cornerRadiusInPixels
    }

    private fun backgroundConfiguration(context: Context) {
        backgroundTintList = ContextCompat.getColorStateList(context, R.color.got_button_background)
    }

    private fun textConfiguration() {
        isAllCaps = false
        setTextAppearance(R.style.GotButtonTextStyle)
    }

    companion object {
        private const val BTN_CORNER_RADIUS_IN_DP = 12
        private const val BTN_VERTICAL_PADDING_IN_DP = 16
        private const val BTN_HORIZONTAL_PADDING_IN_PX = 0
    }
}