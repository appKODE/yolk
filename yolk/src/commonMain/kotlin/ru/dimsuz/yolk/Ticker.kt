package ru.dimsuz.yolk

import kotlin.time.Clock
import kotlin.time.Instant

public interface Ticker {
  public val now: Instant

  public companion object {
    public fun system(): Ticker = object : Ticker {
      override val now: Instant
        get() = Clock.System.now()
    }
  }
}
