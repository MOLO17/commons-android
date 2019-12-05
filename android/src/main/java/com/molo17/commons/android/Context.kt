package com.molo17.commons.android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

fun Context.copy(text: String) {
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    manager.primaryClip = ClipData.newPlainText(text, text)
}
