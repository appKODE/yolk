package ru.dimsuz.yolk

public expect class MemoryKeyStore<K : Any>(
  ticker: Ticker = Ticker.system(),
) : KeyStore<K> {
  override suspend fun read(key: K): KeyTimestamps?
  override suspend fun write(key: K, timestamps: KeyTimestamps)
  override suspend fun update(key: K, transform: (KeyTimestamps?) -> KeyTimestamps?)
  override suspend fun remove(key: K)
}
