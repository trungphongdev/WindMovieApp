package com.example.windmoiveapp.extension

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative


fun View.updateMargin(
    @Px start: Int? = null,
    @Px top: Int? = null,
    @Px end: Int? = null,
    @Px bottom: Int? = null
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        // updateMargins(left = newMarginStart)
        updateMarginsRelative(
            start = start ?: marginStart,
            top = top ?: topMargin,
            end = end ?: marginEnd,
            bottom = bottom ?: bottomMargin
        )
    }
}

fun View.updatePadding(
    @Px start: Int? = null,
    @Px top: Int? = null,
    @Px end: Int? = null,
    @Px bottom: Int? = null
) {
    val startPx = start ?: this.paddingLeft
    val topPx = top ?: this.paddingTop
    val endPx = end ?: this.paddingRight
    val bottomPx = bottom ?: this.paddingBottom
    this.setPadding(startPx, topPx, endPx, bottomPx)
}




fun View.setPaddingView(paddingTop: Int, paddingBottom: Int, paddingStart: Int, paddingEnd: Int) {
    this.setPadding(
        resources.getDimensionPixelOffset(paddingStart),
        resources.getDimensionPixelOffset(paddingTop),
        resources.getDimensionPixelSize(paddingEnd),
        resources.getDimensionPixelSize(paddingBottom)
    )
}



fun View.click(delayMs: Long = 1000L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < delayMs) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}


