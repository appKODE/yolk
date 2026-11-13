package ru.dimsuz.yolk

import java.util.concurrent.ConcurrentHashMap

public actual class MemoryKeyStore<K : Any> actual constructor(
  private val ticker: Ticker,
) : KeyStore<K> {
  private val store = ConcurrentHashMap<K, KeyTimestamps>()

  actual override suspend fun read(key: K): KeyTimestamps? {
    return store.compute(key) { _, ts -> ts?.copy(accessAt = ticker.now) }
  }

  actual override suspend fun write(key: K, timestamps: KeyTimestamps) {
    store.compute(key) { _, ts -> ts?.copy(updatedAt = ticker.now) }
  }

  actual override suspend fun update(key: K, transform: (KeyTimestamps?) -> KeyTimestamps?) {
    store.compute(key) { _, ts -> transform(ts) }
  }

  actual override suspend fun remove(key: K) {
    store.remove(key)
  }
}
