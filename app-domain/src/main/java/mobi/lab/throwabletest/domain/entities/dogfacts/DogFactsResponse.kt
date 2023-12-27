package mobi.lab.throwabletest.domain.entities.dogfacts

/**
 * Created by siret on 27.12.2023.
 */
data class DogFactsResponse(
    val facts: List<String>,
    val success: Boolean
)
