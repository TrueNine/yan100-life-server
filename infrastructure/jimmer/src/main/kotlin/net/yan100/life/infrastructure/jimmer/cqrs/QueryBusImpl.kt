package net.yan100.life.infrastructure.jimmer.cqrs

import net.yan100.life.application.Query
import net.yan100.life.application.QueryBus
import net.yan100.life.application.QueryHandler
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.isSubclassOf

@Component
class QueryBusImpl(
  private val applicationContext: ApplicationContext,
) : QueryBus {

  override suspend fun <TQuery : Query<TResult>, TResult> send(query: TQuery): TResult {
    @Suppress("UNCHECKED_CAST")
    val handler = findHandler<QueryHandler<TQuery, TResult>>(query::class)
      ?: error("未找到查询处理器: ${query::class.simpleName}")
    return handler.handle(query)
  }

  private inline fun <reified T> findHandler(queryClass: KClass<*>): T? {
    val beans = applicationContext.getBeansOfType(T::class.java)

    return beans.values.firstOrNull { handler ->
      val handlerClass = handler!!::class
      val queryType = getHandledQueryType(handlerClass)
      queryType != null && queryClass.isSubclassOf(queryType)
    }
  }

  private fun getHandledQueryType(handlerClass: KClass<*>): KClass<*>? {
    // 查找实现的处理器接口
    val handlerInterface = handlerClass.allSupertypes.firstOrNull { type ->
      val classifier = type.classifier as? KClass<*>
      classifier != null && classifier == QueryHandler::class
    }

    // 获取泛型参数（查询类型）
    return handlerInterface?.arguments?.firstOrNull()?.type?.classifier as? KClass<*>
  }
} 
