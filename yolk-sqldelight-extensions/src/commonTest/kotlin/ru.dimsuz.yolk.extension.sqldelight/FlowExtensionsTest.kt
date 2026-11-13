package ru.dimsuz.yolk.extension.sqldelight

import app.cash.turbine.test
import app.cash.sqldelight.Query
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant
import ru.dimsuz.yolk.Cache
import ru.dimsuz.yolk.ExpirePolicy
import ru.dimsuz.yolk.MemoryKeyStore
import ru.dimsuz.yolk.MemoryValueStore
import ru.dimsuz.yolk.Ticker
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

class FlowExtensionsTest : ShouldSpec({
  should("emit new value after cache is invalidated second time") {

    val tableData = MutableStateFlow(0L)

    val listeners = mutableListOf<Query.Listener>()
    val query = object : Query<Int>({ it.getLong(0)!!.toInt() }) {
      override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> {
        return mapper(
          object : SqlCursor {
            var count = 0
            override fun next(): QueryResult<Boolean> = QueryResult.Value(count++ == 0)
            override fun getString(index: Int): String? = null
            override fun getLong(index: Int): Long = tableData.value
            override fun getBytes(index: Int): ByteArray? = null
            override fun getDouble(index: Int): Double? = null
            override fun getBoolean(index: Int): Boolean? = null
          }
        )
      }

      override fun addListener(listener: Listener) {
        listeners.add(listener)
      }

      override fun removeListener(listener: Listener) {
        listeners.remove(listener)
      }
    }

    val ticker = Ticker.fake()
    val cache = Cache<Int, Unit>(
      expirePolicy = ExpirePolicy.afterWrite(1.hours, ticker),
      fetch = { key ->
        println("fetching key")
        tableData.update { it + 1 }
        ticker.advanceBy(200.milliseconds)
        listeners.toList().forEach { it.queryResultsChanged() }
        ticker.advanceBy(200.milliseconds)
      },
      keyStore = MemoryKeyStore(ticker),
      valueStore = MemoryValueStore(),
      ticker = ticker,
    )

    query
      .asCachedFlow(cache, keyFactory = { 3 })
      .map { it.executeAsOne() }
      .test {
        cache.load(key = 3)
        awaitItem() shouldBe 1
        ticker.advanceBy(2.hours)
        cache.load(key = 3)
        awaitItem() shouldBe 2
        cancelAndIgnoreRemainingEvents()
      }
  }
})

fun Ticker.Companion.fake(): FakeTicker {
  return FakeTicker()
}

class FakeTicker(override var now: Instant = Instant.fromEpochMilliseconds(0)) : Ticker {
  fun advanceBy(duration: Duration) {
    now += duration
  }
}
