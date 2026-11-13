package ru.dimsuz.yolk

import java.util.concurrent.ConcurrentHashMap

public actual class MemoryValueStore<K : Any, V> : ValueStore<K, V> {
  private val store = ConcurrentHashMap<K, V>()

  actual override suspend fun read(key: K): V? {
    return store[key]
  }

  actual override suspend fun write(key: K, value: V) {
    store[key] = value
  }

  actual override suspend fun remove(key: K) {
    store.remove(key)
  }
}
