package net.yan100.life.domain

import net.yan100.compose.RefId
import net.yan100.compose.datetime
import net.yan100.compose.slf4j
import net.yan100.compose.toId
import java.util.*

/**
 * 聚合根 ID 基类，所有聚合根ID都应继承自此类
 */
sealed class AggregateId(
  protected open val _id: RefId? = null,
  protected open val _tenantId: String? = null,
  protected open val _createAt: datetime? = null,
  protected open val _updateAt: datetime? = null,
  protected open val _version: Int? = null,
) {
  constructor(
    stringId: String,
    tenantId: String? = null,
    createAt: datetime? = null,
    updateAt: datetime? = null,
    version: Int? = null,
  ) : this(
    _id = stringId.toId() ?: error("invalid id $stringId"), _tenantId = tenantId, _createAt = createAt, _updateAt = updateAt, _version = version
  )

  open val activated: Boolean
    get() = when (this) {
      is Create -> false
      else -> true
    }

  abstract class Create(
    open val tenantId: String? = null,
    open val createAt: datetime? = datetime.now(),
    open val version: Int? = null,
    open val uniqueCreateId: String
  ) : AggregateId(
    _tenantId = tenantId,
    _createAt = datetime.now(),
    _version = version,
  ) {
    internal data class Default(
      override val tenantId: String? = null,
      override val createAt: datetime = datetime.now(),
      override val version: Int? = null,
      override val uniqueCreateId: String = UUID.randomUUID().toString(),
    ) : Create(
      tenantId = tenantId,
      createAt = createAt,
      version = version,
      uniqueCreateId = uniqueCreateId,
    )
  }

  abstract class Query(
    open val id: RefId,
    open val tenantId: String? = null,
  ) : AggregateId(
    _id = id, _tenantId = tenantId
  ) {
    internal data class Default(
      override val id: RefId, override val tenantId: String? = null,
    ) : Query(
      id = id, tenantId = tenantId
    ) {
      constructor(
        stringId: String,
        tenantId: String? = null,
      ) : this(
        id = stringId.toId() ?: error("invalid id value $stringId"), tenantId = tenantId
      )
    }
  }

  abstract class Result(
    open val id: RefId,
    open val tenantId: String? = null,
    open val version: Int,
    open val createAt: datetime,
    open val updateAt: datetime,
  ) : AggregateId(
    _id = id, _tenantId = tenantId, _version = version, _createAt = createAt, _updateAt = updateAt
  ) {
    constructor(
      stringId: String,
      tenantId: String? = null,
      version: Int,
      createAt: datetime,
      updateAt: datetime,
    ) : this(
      id = stringId.toId() ?: error("invalid id value $stringId"), tenantId = tenantId, version = version, createAt = createAt, updateAt = updateAt
    )

    internal data class Default(
      override val id: RefId,
      override val tenantId: String? = null,
      override val createAt: datetime,
      override val updateAt: datetime,
      override val version: Int,
    ) : Result(
      id = id, tenantId = tenantId, createAt = createAt, updateAt = updateAt, version = version
    )
  }

  abstract class Change(
    open val id: RefId,
    open val tenantId: String? = null,
    open val updateAt: datetime? = datetime.now(),
    open val version: Int? = null,
  ) : AggregateId(
    _id = id,
    _tenantId = tenantId,
    _updateAt = updateAt ?: datetime.now(),
    _version = version
  ) {
    constructor(
      stringId: String, tenantId: String? = null, updateAt: datetime? = datetime.now(), version: Int? = null,
    ) : this(
      id = stringId.toId() ?: error("invalid id value $stringId"), tenantId = tenantId, updateAt = updateAt, version = version
    )

    internal data class Default(
      override val id: RefId,
      override val tenantId: String? = null,
      override val updateAt: datetime? = datetime.now(),
      override val version: Int? = null,
    ) : Change(
      id = id,
      tenantId = tenantId,
      updateAt = updateAt,
      version = version
    )
  }

  fun toQueryId(
    tenantId: String? = null,
  ): Query {
    return Query.Default(
      id = _id ?: error("[toQueryId] id is null"),
      tenantId = tenantId ?: _tenantId,
    )
  }

  fun toCreateId(
    tenantId: String? = null,
    createAt: datetime = datetime.now(),
  ): Create {
    return Create.Default(
      tenantId = tenantId,
      createAt = createAt,
    )
  }

  fun toChangeId(
    tenantId: String? = null,
    updateAt: datetime? = datetime.now(),
    version: Int = 0,
  ): Change {
    return when (this) {
      is Create -> error("[toChangeId] invalid type CreateId to ChangeId, id not active")
      else -> Change.Default(
        id = _id ?: error("[toChangeId] invalid id value: $_id"),
        tenantId = tenantId,
        updateAt = updateAt,
        version = version,
      )
    }
  }

  fun toResultId(): Result {
    return Result.Default(
      id = _id ?: error("id is null"),
      tenantId = _tenantId,
      createAt = _createAt ?: error("createAt is null"),
      updateAt = _updateAt ?: error("updateAt is null"),
      version = _version ?: error("version is null"),
    )
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AggregateId) return false
    if (_id != other._id) return false
    if (_version != other._version) return false
    if (_tenantId != other._tenantId) return false
    if (_createAt != other._createAt) return false
    if (_updateAt != other._updateAt) return false
    return true
  }

  override fun hashCode(): Int {
    var result = _id.hashCode()
    result = 31 * result + (_version ?: 0)
    result = 31 * result + (_tenantId?.hashCode() ?: 0)
    result = 31 * result + (_createAt?.hashCode() ?: 0)
    result = 31 * result + (_updateAt?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "AggregateId(value=$_id, tenantId=$_tenantId, createAt=$_createAt, updateAt=$_updateAt, lockVersion=$_version)"
  }

  companion object {
    @JvmStatic
    private val log = slf4j<AggregateId>()

    @JvmStatic
    fun result(
      id: RefId,
      tenantId: String? = null,
      createAt: datetime,
      updateAt: datetime,
      version: Int,
    ): Result {
      log.trace("create new aggregate result id")
      return Result.Default(
        id = id,
        tenantId = tenantId,
        createAt = createAt,
        updateAt = updateAt,
        version = version,
      )
    }

    @JvmStatic
    @JvmName("resultByStringId")
    fun resultByStringId(
      stringId: String,
      tenantId: String? = null,
      createAt: datetime,
      updateAt: datetime,
      version: Int,
    ): Result {
      return result(
        id = stringId.toId() ?: error("invalid id value $stringId"),
        tenantId = tenantId,
        createAt = createAt,
        updateAt = updateAt,
        version = version,
      )
    }

    @JvmStatic
    @JvmOverloads
    fun change(
      id: RefId,
      tenantId: String? = null,
      updateAt: datetime? = datetime.now(),
      version: Int? = null,
    ): Change {
      return Change.Default(
        id = id,
        tenantId = tenantId,
        updateAt = updateAt,
        version = version
      )
    }

    @JvmStatic
    @JvmOverloads
    fun change(
      stringId: String,
      tenantId: String? = null,
      updateAt: datetime? = datetime.now(),
      version: Int? = null,
    ): Change {
      return change(
        id = stringId.toId() ?: error("invalid id value $stringId"),
        tenantId = tenantId,
        updateAt = updateAt,
        version = version
      )
    }

    @JvmStatic
    @JvmOverloads
    fun query(
      id: RefId,
      tenantId: String? = null,
    ): Query {
      return Query.Default(
        id = id, tenantId = tenantId
      )
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("queryByStringId")
    fun queryByStringId(
      stringId: String,
      tenantId: String? = null,
    ): Query {
      return query(
        id = stringId.toId() ?: error("invalid id value $stringId"),
        tenantId = tenantId
      )
    }

    @JvmStatic
    @JvmOverloads
    fun create(
      tenantId: String? = null,
      createAt: datetime = datetime.now(),
    ): Create {
      log.trace("create new aggregate, tenantId: {}, create datetime: {}", tenantId, createAt)
      return Create.Default(
        tenantId = tenantId,
        createAt = createAt
      )
    }
  }
}
