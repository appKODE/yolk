package ru.dimsuz.yolk

import kotlin.time.Instant

public data class KeyTimestamps(
  val accessAt: Instant?,
  val updatedAt: Instant?,
)
