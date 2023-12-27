package mobi.lab.throwabletest.infrastructure.dogfacts.remote.dto

/**
 * Created by siret on 27.12.2023.
 */
data class ApiDogFactsResponse(
    val facts: List<String>,
    val success: Boolean
)
