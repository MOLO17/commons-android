package com.molo17.commons.android

import android.app.Activity
import androidx.appcompat.app.AlertDialog

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

fun Activity.showErrorAlert(msg: String) {
    AlertDialog.Builder(this)
        .setTitle(R.string.error)
        .setMessage(msg)
        .setPositiveButton(R.string.accept) { _, _ -> }
        .show()
}


fun Activity.showWarningAlert(msg: String) {
    AlertDialog.Builder(this)
        .setTitle(R.string.warning)
        .setMessage(msg)
        .setPositiveButton(R.string.accept) { _, _ -> }
        .show()
}