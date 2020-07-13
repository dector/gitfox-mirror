package gitfox.model.data.cache

import com.github.aakira.napier.Napier
import gitfox.util.currentTimeMillis
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

abstract class ExpirableCache<Key, Value> constructor(
    private val lifetime: Long
) {

    abstract val itemType: String

    private data class CacheItem<Value>(val time: Long, val data: Value)

    private val cache = mutableMapOf<Key, CacheItem<Deferred<Value>>>()

    fun clear() {
        Napier.d("Clear $itemType cache")
        cache.clear()
    }

    fun put(key: Key, data: Value) {
        Napier.d("Put $itemType")
        cache.put(key, CacheItem(currentTimeMillis(), GlobalScope.async { data }))
    }

    fun get(key: Key): Value? {
        val item = cache[key]
        if (item == null || currentTimeMillis() - item.time > lifetime) {
            Napier.d("Get NULL project($key) $itemType")
            return null
        } else {
            Napier.d("Get CACHED project($key) $itemType")
            return item.data.getCompleted()
        }
    }

    suspend fun getOrPut(key: Key, valueFactory: suspend () -> Value): Value {
        val value = cache[key]
        return if (value != null) {
            value.data.await()
        } else {
            val newValue = GlobalScope.async {
                try {
                    valueFactory()
                } catch (e: Exception) {
                    cache.remove(key)
                    throw e
                }
            }
            cache[key] = CacheItem(currentTimeMillis(), newValue)
            newValue.await()
        }
    }
}