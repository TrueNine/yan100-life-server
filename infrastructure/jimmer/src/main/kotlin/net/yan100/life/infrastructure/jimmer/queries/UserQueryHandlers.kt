package net.yan100.life.infrastructure.jimmer.queries

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.yan100.compose.Pr
import net.yan100.compose.rds.entities.crd
import net.yan100.compose.rds.fetchPq
import net.yan100.life.application.QueryHandler
import net.yan100.life.application.dto.UserDetailDto
import net.yan100.life.application.dto.UserView
import net.yan100.life.application.user.queries.GetUserByAccountQuery
import net.yan100.life.application.user.queries.GetUserDetailQuery
import net.yan100.life.application.user.queries.SearchUsersQuery
import net.yan100.life.infrastructure.jimmer.entities.*
import net.yan100.life.infrastructure.jimmer.repositories.IUserAccountRepo
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GetUserDetailQueryHandler(
  private val sqlClient: KSqlClient,
  private val userRepo: IUserAccountRepo,
) : QueryHandler<GetUserDetailQuery, UserDetailDto?> {

  override suspend fun handle(query: GetUserDetailQuery): UserDetailDto? = withContext(Dispatchers.IO) {
    userRepo
      .findByIdOrNull(query.userId)
      ?.toUserDetailDto()
  }

  private fun UserAccount.toUserDetailDto(): UserDetailDto {
    return UserDetailDto(
      id = id.toString(),
      account = account,
      nickName = nickName,
      phone = phone,
      avatarUrl = avatarUrl,
      hasWechatBound = !wechatWxpaOpenId.isNullOrEmpty(),
      favoritePostIds = favoritePostContents.map { it.id.toString() },
    )
  }
}

@Service
class GetUserByAccountQueryHandler(
  private val userRepo: IUserAccountRepo,
) : QueryHandler<GetUserByAccountQuery, UserView?> {

  override suspend fun handle(query: GetUserByAccountQuery): UserView? = withContext(Dispatchers.IO) {
    userRepo.findByAccount(query.account)?.toUserDto()
  }

  private fun UserAccount.toUserDto(): UserView {
    return UserView(
      id = id.toString(),
      account = account,
      nickName = nickName,
      phone = phone,
      avatarUrl = avatarUrl,
      hasWechatBound = !wechatWxpaOpenId.isNullOrEmpty(),
    )
  }
}

@Service
class SearchUsersQueryHandler(
  private val sqlClient: KSqlClient,
) : QueryHandler<SearchUsersQuery, Pr<UserView>> {

  override suspend fun handle(query: SearchUsersQuery): Pr<UserView> = withContext(Dispatchers.IO) {
    val keyword = query.keyword

    val totalCount = sqlClient
      .createQuery(UserAccount::class) {
        if (!keyword.isNullOrBlank()) {
          where(
            or(
              table.account like "%$keyword%",
              table.nickName like "%$keyword%",
              table.phone like "%$keyword%"
            )
          )
        }
        select(count(table))
      }
      .fetchOne()

    sqlClient
      .createQuery(UserAccount::class) {
        if (!keyword.isNullOrBlank()) {
          where(
            or(
              table.account like "%$keyword%",
              table.nickName like "%$keyword%",
              table.phone like "%$keyword%"
            )
          )
        }
        orderBy(table.crd.desc())
        select(table)
      }
      .fetchPq(pq = query.pq) {
        UserView(
          id = it.id.toString(),
          account = it.account,
          nickName = it.nickName,
          phone = it.phone,
          avatarUrl = it.avatarUrl,
          hasWechatBound = !it.wechatWxpaOpenId.isNullOrEmpty(),
        )
      }
  }

  private fun UserAccount.toUserDto(): UserView {
    return UserView(
      id = id.toString(),
      account = account,
      nickName = nickName,
      phone = phone,
      avatarUrl = avatarUrl,
      hasWechatBound = !wechatWxpaOpenId.isNullOrEmpty(),
    )
  }
} 
