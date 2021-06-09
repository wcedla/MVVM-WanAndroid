package com.istrong.wcedla.base.extension

import com.istrong.wcedla.base.utils.PlatformUtil


/**
 * Convert dp to px.
 */
val Int.dp: Int
    get() {
        val scale = PlatformUtil.application.resources.displayMetrics.density
        return (this * scale + 0.5).toInt()
    }

val Float.dp: Float
    get() {
        val scale = PlatformUtil.application.resources.displayMetrics.density
        return this * scale
    }

val Double.dp: Double
    get() {
        val scale = PlatformUtil.application.resources.displayMetrics.density
        return this * scale
    }
