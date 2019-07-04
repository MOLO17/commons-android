package com.molo17.commons.android

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Damiano Giusti on 2019-06-03.
 */
class KtTextWatcher(
    private val afterTextChanged: ((s: Editable) -> Unit)? = null,
    private val beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    private val onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        s ?: return
        afterTextChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        s ?: return
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s ?: return
        onTextChanged?.invoke(s, start, before, count)
    }
}