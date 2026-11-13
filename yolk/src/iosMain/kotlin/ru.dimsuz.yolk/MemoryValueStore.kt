package ru.dimsuz.yolk

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public actual class MemoryValueStore<K : Any, V> : ValueStore<K, V> {
  private val store = HashMap<K, V>()
  private val mutex = Mutex()

  actual override suspend fun read(key: K): V? {
    return mutex.withLock {
      store[key]
    }
  }

  actual override suspend fun write(key: K, value: V) {
    mutex.withLock {
      store[key] = value
    }
  }

  actual override suspend fun remove(key: K) {
    mutex.withLock {
      store.remove(key)
    }
  }
}
