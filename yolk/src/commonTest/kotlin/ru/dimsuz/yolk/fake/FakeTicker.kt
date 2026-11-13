package ru.dimsuz.yolk.fake

import kotlin.time.Instant
import ru.dimsuz.yolk.Ticker
import kotlin.time.Duration

fun Ticker.Companion.fake(): FakeTicker {
  return FakeTicker()
}

class FakeTicker(override var now: Instant = Instant.fromEpochMilliseconds(0)) : Ticker {
  fun advanceBy(duration: Duration) {
    now += duration
  }
}
