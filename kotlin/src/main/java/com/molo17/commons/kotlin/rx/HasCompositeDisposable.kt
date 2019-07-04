package com.molo17.commons.kotlin.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by Damiano Giusti on 2019-05-14.
 */
interface HasCompositeDisposable {
    val compositeDisposable: CompositeDisposable
    fun Disposable.bind(): Boolean = compositeDisposable.add(this)
    fun dispose() = compositeDisposable.clear()
}

@Suppress("FunctionName")
fun HasCompositeDisposable(): HasCompositeDisposable = object :
    HasCompositeDisposable {
    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
}