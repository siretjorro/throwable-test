package mobi.lab.throwabletest.infrastructure.common.remote.error

import androidx.annotation.Keep
import se.ansman.kotshi.JsonSerializable
import java.time.Instant

/**
 * A sample REST API error response dto. Modify or remove this as needed based on the project requirements and API specifications.
 */
@Keep
@JsonSerializable
data class ApiErrorResponse(
    val httpStatus: Int,
    val httpError: String,
    val instant: Instant?,
    val code: String?,
    val message: String?,
)
