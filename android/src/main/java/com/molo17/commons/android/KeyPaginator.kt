package com.molo17.commons.android

/**
 * Interface which provides a common abstraction on top of a common pagination flow.
 *
 * In the signature, [K] represents the Key which identifies the page, and [T] the data type of an item in the page.
 * Each page is composed by a list of [T] items.
 *
 * The first method invoked is [onLoadInitial], which is responsible to deliver
 * the initial data using the [InitialLoadCallback].
 *
 * Based on further behavior, the [onLoadAfter] and [onLoadBefore] may be called for loading additional data,
 * identifier by the given key of type [K].
 */
interface KeyPaginator<K, T> {

    /**
     * Loads the initial dataset and delivers it by invoking the provided callback.
     * @param callback the callback used for delivering the results
     */
    fun onLoadInitial(callback: InitialLoadCallback<K, T>)

    /**
     * Loads a page of the dataset identified by the given key, and delivers it by invoking the provided callback.
     * @param key the key which identifies the dataset page to load
     * @param callback the callback used for delivering the results
     */
    fun onLoadBefore(key: K, callback: LoadCallback<K, T>)

    /**
     * Loads a page of the dataset identified by the given key, and delivers it by invoking the provided callback.
     * @param key the key which identifies the dataset page to load
     * @param callback the callback used for delivering the results
     */
    fun onLoadAfter(key: K, callback: LoadCallback<K, T>)

    /**
     * Refreshes the currently displayed data. For example, if the datasource is backed by a cache
     * then this method implementation is going to invalidate it.
     */
    fun refresh(callback: () -> Unit)

    interface InitialLoadCallback<K, T> {
        fun onLoading() {}
        fun onLoaded(results: List<T>, previousPageKey: K?, nextPageKey: K?)
        fun onError(error: Throwable)
    }

    interface LoadCallback<K, T> {
        fun onLoading() {}
        fun onLoaded(results: List<T>, subsequentPageKey: K?)
        fun onError(error: Throwable)
    }
}