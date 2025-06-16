package net.yan100.life.infrastructure.jimmer

import net.yan100.compose.rds.entities.IEntity
import net.yan100.life.domain.AggregateId

fun IEntity.toAggregateRootResultId(
  tenantId: String? = null,
): AggregateId.Result {
  return AggregateId.result(
    id = this.id,
    tenantId = tenantId,
    createAt = crd!!,
    updateAt = mrd!!,
    version = rlv
  )
}
