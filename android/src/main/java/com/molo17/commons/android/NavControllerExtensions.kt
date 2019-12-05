package com.molo17.commons.android

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

/**
 * Safely tries to navigate to a direction. Invokes the [onError] argument if an error occurred.
 *
 * @param directions The directions where to navigate
 * @param onError The error lambda invoked when an error occurred
 */
fun NavController.tryNavigate(directions: NavDirections, onError: (Throwable) -> Unit) = try {
    navigate(directions)
} catch (e: IllegalArgumentException) {
    // Sometimes navigation destinations seem not to be found. An IllegalArgumentException like:
    // `navigation destination [...] is unknown to this NavController`
    // is thrown. This method is meant to overcome the issue.
    onError(e)
}

/**
 * Safely tries to navigate to a direction. Invokes the [onError] argument if an error occurred.
 *
 * @param resId an [action][NavDestination.getAction] id or a destination id to
 * navigate to
 * @param args arguments to pass to the destination
 * @param navOptions special options for this navigation operation
 * @param onError The error lambda invoked when an error occurred
 */
fun NavController.tryNavigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?, onError: (Throwable) -> Unit) = try {
    navigate(resId, args, navOptions)
} catch (e: IllegalArgumentException) {
    // Sometimes navigation destinations seem not to be found. An IllegalArgumentException like:
    // `navigation destination [...] is unknown to this NavController`
    // is thrown. This method is meant to overcome the issue.
    onError(e)
}