package net.yan100.life.application.user.queries

import net.yan100.compose.Pq
import net.yan100.compose.Pr
import net.yan100.compose.RefId
import net.yan100.life.application.Query
import net.yan100.life.application.dto.UserDetailDto
import net.yan100.life.application.dto.UserView

/**
 * 获取用户详情查询
 */
data class GetUserDetailQuery(
  val userId: RefId,
) : Query<UserDetailDto?>

/**
 * 通过账号查询用户
 */
data class GetUserByAccountQuery(
  val account: String,
) : Query<UserView?>

/**
 * 通过手机号查询用户
 */
data class GetUserByPhoneQuery(
  val phone: String,
) : Query<UserView?>

/**
 * 搜索用户查询
 */
data class SearchUsersQuery(
  val keyword: String? = null,
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<UserView>>
