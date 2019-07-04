package com.molo17.commons.android

import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.TextView

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

fun TextView.setAsPassword() {
    transformationMethod = PasswordTransformationMethod()
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
}

fun lowercaseInputFilter() = InputFilter { source, _, _, _, _, _ -> source.toString().toLowerCase() }