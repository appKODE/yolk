package ru.dimsuz.yolk

public expect class MemoryValueStore<K : Any, V>() : ValueStore<K, V> {
  override suspend fun read(key: K): V?
  override suspend fun write(key: K, value: V)
  override suspend fun remove(key: K)
}
