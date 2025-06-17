package net.yan100.life.domain

import net.yan100.compose.RefId
import net.yan100.compose.datetime
import net.yan100.compose.slf4j
import net.yan100.compose.toId
import java.util.*

/**
 * Represents a unique identifier for an aggregate in a domain-driven design context.
 * Encapsulates the identity and metadata of an aggregate, including tenant information,
 * creation and update timestamps, versioning, and activation status.
 *
 * Provides methods to transform the aggregate identifier into various domain-specific
 * identifier types such as query, create, change, and result identifiers. These methods
 * allow for flexible usage in different contexts while maintaining consistency in identity.
 *
 * The class overrides equality, hash code, and string representation to ensure proper
 * comparison and debugging support.
 *
 * toQueryId transforms the aggregate identifier into a query identifier, optionally
 * allowing specification of a tenant ID.
 *
 * toCreateId transforms the aggregate identifier into a create identifier, with optional
 * tenant ID and creation timestamp parameters.
 *
 * toChangeId transforms the aggregate identifier into a change identifier, supporting
 * optional tenant ID, update timestamp, and version parameters.
 *
 * toResultId transforms the aggregate identifier into a result identifier, representing
 * the outcome or state of the aggregate.
 *
 * equals determines whether this instance is equal to another object based on its
 * properties.
 *
 * hashCode generates a hash code consistent with the equality contract.
 *
 * toString provides a string representation of the aggregate identifier for debugging
 * or logging purposes.
 * @param _id The unique identifier for the aggregate.
 * @param _tenantId The tenant identifier associated with the aggregate.
 * @param _createAt The timestamp indicating when the aggregate was created.
 * @param _updateAt The timestamp indicating when the aggregate was last updated.
 * @param _version The version number indicating the current state of the aggregate.
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
    _id = stringId.toId() ?: error("invalid number value id $stringId"),
    _tenantId = tenantId,
    _createAt = createAt,
    _updateAt = updateAt,
    _version = version
  )

  /**
   * Indicates whether the current aggregate identifier is in an activated state.
   * The value is determined based on the type of the aggregate identifier:
   * - Returns `false` if the identifier is of type [Create], indicating it represents a creation state.
   * - Returns `true` for all other types, indicating the identifier is active or represents a non-creation state.
   *
   * This property is primarily used to enforce business rules related to the activation status of aggregate roots
   * and their identifiers. It ensures that operations requiring an active state are only performed when appropriate.
   */
  open val activated: Boolean
    get() = when (this) {
      is Create -> false
      else -> true
    }

  /**
   * Abstract class representing the creation identifier for an aggregate.
   * It encapsulates the necessary metadata required to uniquely identify and track the creation of an aggregate.
   * This class extends [AggregateId] to inherit common identifier properties and behavior.
   *
   * @param [tenantId] represents the optional tenant context associated with the creation.
   * @param [createAt] denotes the timestamp when the creation occurred, defaulting to the current time if not provided.
   * @param [version] indicates the optional version of the aggregate at the time of creation.
   * @param [uniqueCreateId] is a mandatory unique identifier specifically for the creation event.
   *
   * This class is designed to be extended by concrete implementations, such as the internal [Default] class,
   * which provides a standardized way to generate creation identifiers.
   *
   * Note: This class is part of the domain model and is used in conjunction with other domain components
   * such as [AggregateRoot], [DomainEvent], and [IDomainRepository].
   */
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
    _id = id,
    _tenantId = tenantId
  ) {
    internal data class Default(
      override val id: RefId,
      override val tenantId: String? = null,
    ) : Query(
      id = id,
      tenantId = tenantId
    ) {
      /**
       * Secondary constructor for creating an instance of [Default] using a string-based ID.
       * Converts the provided [stringId] into a valid ID using the `toId` extension function.
       * If the conversion fails, an exception is thrown with a detailed error message.
       *
       * The optional [tenantId] parameter allows specifying a tenant identifier, which is passed through to the primary constructor.
       * This constructor delegates to the primary constructor after validating and converting the [stringId].
       * @param stringId The string representation of the ID to be converted into a [RefId].
       * @param tenantId The optional tenant identifier associated with the query.
       * @throws IllegalArgumentException If the provided [stringId] cannot be converted into a valid
       */
      constructor(
        stringId: String,
        tenantId: String? = null,
      ) : this(
        id = stringId.toId() ?: error("invalid number value id $stringId"),
        tenantId = tenantId
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
    _id = id,
    _tenantId = tenantId,
    _version = version,
    _createAt = createAt,
    _updateAt = updateAt
  ) {
    /**
     * Secondary constructor for creating a [Result] instance using a string-based ID.
     * Converts the provided [stringId] into a [RefId] using the `toId` extension function.
     * If the conversion fails, an error is thrown with a message indicating the invalid value.
     *
     * @param stringId The string representation of the ID to be converted into a [RefId].
     * @param tenantId Optional tenant identifier associated with the result.
     * @param version The version number of the result.
     * @param createAt The timestamp indicating when the result was created.
     * @param updateAt The timestamp indicating when the result was last updated.
     *
     * This constructor delegates to the primary constructor of [Result] after performing the ID conversion.
     */
    constructor(
      stringId: String,
      tenantId: String? = null,
      version: Int,
      createAt: datetime,
      updateAt: datetime,
    ) : this(
      id = stringId.toId() ?: error("invalid number value id $stringId"),
      tenantId = tenantId,
      version = version,
      createAt = createAt,
      updateAt = updateAt
    )

    internal data class Default(
      override val id: RefId,
      override val tenantId: String? = null,
      override val createAt: datetime,
      override val updateAt: datetime,
      override val version: Int,
    ) : Result(
      id = id,
      tenantId = tenantId,
      createAt = createAt,
      updateAt = updateAt,
      version = version
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
    /**
     * Secondary constructor for creating an instance of [Change] using a string-based ID.
     *
     * This constructor converts the provided [stringId] into a [RefId] using the `toId` extension function.
     * If the conversion fails, an exception is thrown with a message indicating the invalid ID value.
     *
     * The [tenantId], [updateAt], and [version] parameters are optional and default to null, the current timestamp,
     * and null respectively. These values are passed to the primary constructor of the [Change] class.
     *
     * @param stringId The string representation of the ID to be converted into a [RefId].
     * @param tenantId The optional tenant identifier associated with the change.
     * @param updateAt The optional timestamp indicating when the change was last updated. Defaults to the current time.
     * @param version The optional version number associated with the change.
     */
    constructor(
      stringId: String,
      tenantId: String? = null, updateAt: datetime? = datetime.now(), version: Int? = null,
    ) : this(
      id = stringId.toId() ?: error("invalid number value id $stringId"),
      tenantId = tenantId,
      updateAt = updateAt,
      version = version
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

    /**
     * Creates a [Result] instance using a string-based identifier and additional metadata.
     *
     * @param stringId the string representation of the identifier, which will be converted to a numeric ID internally
     * @param tenantId an optional tenant identifier associated with the result, defaulting to null if not provided
     * @param createAt the timestamp indicating when the result was created
     * @param updateAt the timestamp indicating when the result was last updated
     * @param version the version number of the result, used for tracking changes
     * @return a [Result] instance initialized with the provided parameters, or throws an error if the stringId is invalid
     */
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
        id = stringId.toId() ?: error("invalid number value id $stringId , stringId cannot convert to number"),
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

    /**
     * Creates a new [Change] instance using the provided parameters.
     *
     * This function converts the given [stringId] into an [RefId] and uses it along with the optional
     * [tenantId], [updateAt], and [version] to create a [Change] object. If the [stringId] cannot be
     * converted into a valid [RefId], an exception is thrown.
     *
     * @param stringId The string representation of the ID to be converted into an [RefId].
     * @param tenantId Optional tenant identifier associated with the change.
     * @param updateAt Optional timestamp indicating when the change occurred. Defaults to the current time.
     * @param version Optional version number associated with the change.
     * @return A new [Change] instance representing the specified parameters.
     * @throws IllegalArgumentException If the [stringId] cannot be converted into a valid [RefId].
     */
    @JvmStatic
    @JvmOverloads
    fun change(
      stringId: String,
      tenantId: String? = null,
      updateAt: datetime? = datetime.now(),
      version: Int? = null,
    ): Change {
      return change(
        id = stringId.toId() ?: error("invalid number value id $stringId , stringId cannot convert to number"),
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
        id = stringId.toId() ?: error("invalid number value id $stringId , stringId cannot convert to number"),
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
