package ru.terrakok.gitlabclient.model.data.cache

import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

abstract class ExpirableCache<Key, Value> constructor(
    private val lifetime: Long
) {

    abstract val itemType: String

    private data class CacheItem<Value>(val time: Long, val data: Value)

    private val cache = ConcurrentHashMap<Key, CacheItem<Value>>()

    fun clear() {
        Timber.d("Clear $itemType cache")
        cache.clear()
    }

    fun put(key: Key, data: Value) {
        Timber.d("Put $itemType")
        cache.put(key, CacheItem(System.currentTimeMillis(), data))
    }

    fun get(key: Key): Value? {
        val item = cache[key]
        if (item == null || System.currentTimeMillis() - item.time > lifetime) {
            Timber.d("Get NULL project($key) $itemType")
            return null
        } else {
            Timber.d("Get CACHED project($key) $itemType")
            return item.data
        }
    }


}