package dev.gabrieldrn.konstellationdemo.ui

import android.content.res.Configuration

/**
 * Returns true if the device is in landscape mode.
 */
val Configuration.isLandscape: Boolean
    get() = orientation == Configuration.ORIENTATION_LANDSCAPE
