package com.molo17.commons.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

/**
 * Common implementation of the [DataSource.Factory] interface provided by the Androidx Paging Library
 * for delegating the loading stuff to a given [KeyPaginator] instance, and mapping the results with a
 * given mapper function.
 *
 * In the signature, [K] represents the key of the dataset pages, [T] is the type of a dataset item, and [R] is
 * the type that will be delivered to the UI, created by the given mapping function.
 *
 * Created by Damiano Giusti on 2019-05-22.
 */
class PagingHelper<K, T, R>(
    private val keyPaginator: KeyPaginator<K, T>,
    private val mapper: (T) -> R
) : DataSource.Factory<K, R>() {

    private val isLoading = MutableLiveData<Boolean>()
    private val errors = MutableLiveData<Throwable>()
    private var currentDataSource: PageKeyedDataSource<K, R>? = null

    /** Whether the underlining data source is loading data. */
    fun isLoading(): LiveData<Boolean> = isLoading

    /** Emits the errors that the underlining data source encountered during processing. */
    fun errors(): LiveData<Throwable> = errors

    /** Refreshes the data, reloading it from scratch. */
    fun refresh() {
        isLoading.postValue(true)
        keyPaginator.refresh {
            currentDataSource?.invalidate() != null
        }
    }

    override fun create(): DataSource<K, R> {
        val datasource = object : PageKeyedDataSource<K, T>() {
            override fun loadInitial(
                params: LoadInitialParams<K>,
                callback: LoadInitialCallback<K, T>
            ) {
                keyPaginator.onLoadInitial(object : KeyPaginator.InitialLoadCallback<K, T> {
                    override fun onLoaded(results: List<T>, previousPageKey: K?, nextPageKey: K?) {
                        isLoading.postValue(false)
                        callback.onResult(results, previousPageKey, nextPageKey)
                    }

                    override fun onLoading() {
                        isLoading.postValue(true)
                    }

                    override fun onError(error: Throwable) {
                        isLoading.postValue(false)
                        errors.postValue(error)
                    }
                })
            }

            override fun loadAfter(params: LoadParams<K>, callback: LoadCallback<K, T>) {
                keyPaginator.onLoadAfter(params.key, object : KeyPaginator.LoadCallback<K, T> {
                    override fun onLoaded(results: List<T>, subsequentPageKey: K?) {
                        callback.onResult(results, subsequentPageKey)
                    }

                    override fun onError(error: Throwable) {
                        isLoading.postValue(false)
                        errors.postValue(error)
                    }
                })
            }

            override fun loadBefore(params: LoadParams<K>, callback: LoadCallback<K, T>) {
                keyPaginator.onLoadBefore(params.key, object : KeyPaginator.LoadCallback<K, T> {
                    override fun onLoaded(results: List<T>, subsequentPageKey: K?) {
                        callback.onResult(results, subsequentPageKey)
                    }

                    override fun onError(error: Throwable) {
                        isLoading.postValue(false)
                        errors.postValue(error)
                    }
                })
            }
        }
        return datasource.map(mapper).also { currentDataSource = it }
    }
}