package com.molo17.commons.kotlin.rx

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.Subject

/**
 * Created by Damiano Giusti on 2019-05-13.
 */

object ExhaustiveWhen {
    @Suppress("NOTHING_TO_INLINE")
    inline infix fun <T> exhaustive(other: T): T = other
}

val using: ExhaustiveWhen = ExhaustiveWhen

@Suppress("HasPlatformType")
fun <T> Observable<T>.debug(
    tag: String = "RxDebug",
    logger: ((tag: String, message: String) -> Unit)? = null
) = doOnNext { logger?.invoke(tag, "[${threadName()}] onNext called with = $it") }
    .doOnError { logger?.invoke(tag, "[${threadName()}] Observable in error! $it") }
    .doOnComplete { logger?.invoke(tag, "[${threadName()}] Observable completed") }
    .doOnDispose { logger?.invoke(tag, "[${threadName()}] Observable disposed") }

@Suppress("HasPlatformType")
fun <T> Flowable<T>.debug(
    tag: String = "RxDebug",
    logger: ((tag: String, message: String) -> Unit)? = null
) = doOnNext { logger?.invoke(tag, "[${threadName()}] onNext called with = $it") }
    .doOnError { logger?.invoke(tag, "[${threadName()}] Flowable in error! $it") }
    .doOnComplete { logger?.invoke(tag, "[${threadName()}] Flowable completed") }
    .doOnCancel { logger?.invoke(tag, "[${threadName()}] Flowable disposed") }

@Suppress("HasPlatformType")
fun <T> Single<T>.debug(
    tag: String = "RxDebug",
    logger: ((tag: String, message: String) -> Unit)? = null
) = doOnSuccess { logger?.invoke(tag, "[${threadName()}] onNext called with = $it") }
    .doOnError { logger?.invoke(tag, "[${threadName()}] Single in error! $it") }
    .doOnDispose { logger?.invoke(tag, "[${threadName()}] Single disposed") }

fun threadName(): String = Thread.currentThread().name

/** Shorthand for implicitly emitting a Unit value */
fun Subject<Unit>.onNext() = onNext(Unit)

/**
 * Simple class which represent the coordinates where to draw something
 */
data class DrawPoint(
    val cx: Float,
    val cy: Float
)

private const val MOMENT_ASPECT_RATIO = 1920.0 / 1180.0

/**
 * Function to calculate the coordinates given width/height of a view and x/y as absolute coordinates
 */
fun drawingCoordinates(width: Int, height: Int, x: Double, y: Double): DrawPoint {
    val aspectRatio = MOMENT_ASPECT_RATIO
    val imageRatio = height.toDouble() / width.toDouble()

    val (cx, cy) = if (imageRatio > aspectRatio) {
        val leftCut = (height / aspectRatio - width) / 2
        val originalX = x * height / aspectRatio
        (originalX - leftCut) to (y * height)
    } else {
        val topCut = (width * aspectRatio - height) / 2
        val originalY = y * width * aspectRatio
        (x * width) to (originalY - topCut)
    }
    return DrawPoint(cx.toFloat(), cy.toFloat())
}