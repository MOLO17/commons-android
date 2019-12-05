package com.molo17.commons.android

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

///////////////////////////////////////////////////////////////////////////
// Result handling
///////////////////////////////////////////////////////////////////////////

/**
 * Simple class which represent a basic result response for an Observable stream.
 */
data class RxResult<out T> internal constructor(
    val value: T? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

/**
 * Applies the [RxResult] pattern to the underlying Rx stream.
 */
fun <T> Observable<T>.toResult(): Observable<RxResult<T>> = this
    .map { RxResult(value = it) }
    .startWith(RxResult(isLoading = true))
    .onErrorResumeNext { error: Throwable -> Observable.just(RxResult(error = error)) }

/**
 * Applies the [RxResult] pattern to the underlying Rx stream.
 */
fun <T> Single<T>.toResult(): Observable<RxResult<T>> = toObservable().toResult()

fun <T> Observable<RxResult<T>>.subscribeBy(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    onLoaded: (() -> Unit)? = null,
    onCompleted: (() -> Unit)? = null
): Disposable = this
    .subscribe({ (value, isLoading, error) ->
        if (isLoading) {
            onLoading?.invoke()
        } else {
            onLoaded?.invoke()
            if (value != null) {
                onSuccess?.invoke(value)
            } else {
                error?.let { onError?.invoke(it) }
            }
        }
    }, { error ->
        onError?.invoke(error)
    }, {
        onCompleted?.invoke()
    })

/**
 * Applies the [RxResult] pattern to the underlying Rx stream.
 */
fun <T> Observable<T>.subscribeForResult(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    onLoaded: (() -> Unit)? = null,
    onCompleted: (() -> Unit)? = null
): Disposable = toResult().subscribeBy(onSuccess, onError, onLoading, onLoaded, onCompleted)

/**
 * Applies the [RxResult] pattern to the underlying Rx stream.
 */
fun <T> Single<T>.subscribeForResult(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    onLoaded: (() -> Unit)? = null,
    onCompleted: (() -> Unit)? = null
): Disposable = toObservable().subscribeForResult(onSuccess, onError, onLoading, onLoaded, onCompleted)

fun <T> Flowable<RxResult<T>>.filterSuccess(): Flowable<T> = this
    .filter { it.value != null }
    .map { it.value }

fun <T> Observable<RxResult<T>>.filterSuccess(): Observable<T> = this
    .filter { it.value != null }
    .map { it.value }

fun <T> Flowable<RxResult<T>>.filterFailure(): Flowable<Throwable> = this
    .filter { it.error != null }
    .map { it.error }

fun <T> Observable<RxResult<T>>.filterFailure(): Observable<Throwable> = this
    .filter { it.error != null }
    .map { it.error }