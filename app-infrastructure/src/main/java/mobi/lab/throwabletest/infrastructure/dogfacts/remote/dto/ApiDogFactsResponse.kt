package mobi.lab.throwabletest.infrastructure.dogfacts.remote.dto

import androidx.annotation.Keep
import se.ansman.kotshi.JsonSerializable

/**
 * Created by siret on 27.12.2023.
 */
@Keep
@JsonSerializable
data class ApiDogFactsResponse(
    val facts: List<String>,
    val success: Boolean
)
