package com.molo17.commons.android

import android.app.Activity
import org.jetbrains.anko.alert

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

fun Activity.showErrorAlert(msg: String) {
    alert {
        titleResource = R.string.error
        message = msg
        positiveButton(R.string.accept) {}
    }.show()
}


fun Activity.showWarningAlert(msg: String) {
    alert {
        titleResource = R.string.warning
        message = msg
        positiveButton(R.string.accept) {}
    }.show()
}