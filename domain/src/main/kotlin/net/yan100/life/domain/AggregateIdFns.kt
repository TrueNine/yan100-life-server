package net.yan100.life.domain

fun Long.toAggregateChangeId(
  tenantId: String? = null,
): AggregateId.Change {
  return AggregateId.change(
    id = this,
    tenantId = tenantId
  )
}

fun String.toAggregateChangeId(
  tenantId: String? = null,
): AggregateId.Change {
  return AggregateId.change(
    stringId = this,
    tenantId = tenantId
  )
}
