package mobi.lab.throwabletest.infrastructure.common.remote

/**
 * Created by siret on 27.12.2023.
 */
import mobi.lab.throwabletest.infrastructure.common.http.ErrorTransformer
import javax.inject.Inject

/**
 * A network API client that wraps our network calls:
 *
 * 1Processes thrown exceptions and tries to parse a server error response or our DomainException based on http status codes.
 * @see ErrorTransformer
 *
 */
class WrappedApiClient @Inject constructor(
    private val errorTransformer: ErrorTransformer,
) {
    suspend fun <RESULT> wrap(call: suspend () -> RESULT): RESULT {
        return try {
            request(call)
        } catch (error: Throwable) {
            throw processException(error)
        }
    }

    private suspend fun <RESULT> request(call: suspend () -> RESULT): RESULT {
        try {
            return call.invoke()
        } catch (error: Throwable) {
            throw processException(error)
        }
    }

    private fun processException(error: Throwable): Throwable {
        return errorTransformer.transform(error)
    }
}
