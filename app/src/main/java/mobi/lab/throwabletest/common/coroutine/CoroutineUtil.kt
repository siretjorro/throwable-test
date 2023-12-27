package mobi.lab.throwabletest.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import mobi.lab.throwabletest.common.platform.LogoutMonitor
import mobi.lab.throwabletest.domain.entities.DomainException
import mobi.lab.throwabletest.domain.entities.ErrorCode

/**
 * Created by siret on 27.12.2023.
 */

/**
 * Combines withContext and runCatching into one function call.
 * Calls the given block with the active CoroutineScope as its receiver.
 *
 * Returns a Result object with a Failure item or a success item.
 * In case of LOCAL_UNAUTHORIZED exception, starts a global logout procedure.
 */
suspend inline fun <R> withContextCatching(dispatcher: CoroutineDispatcher, crossinline block: suspend CoroutineScope.() -> R): Result<R> {
    return try {
        withContext(dispatcher) {
            Result.success(block())
        }
    } catch (error: Throwable) {
        if (error is DomainException && error.isFor(ErrorCode.LOCAL_UNAUTHORIZED)) {
            LogoutMonitor.logout()
        }
        Result.failure(error)
    }
}
