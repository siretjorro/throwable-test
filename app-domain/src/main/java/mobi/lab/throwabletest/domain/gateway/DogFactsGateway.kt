package mobi.lab.throwabletest.domain.gateway

import mobi.lab.throwabletest.domain.entities.dogfacts.DogFactsResponse

/**
 * Created by siret on 27.12.2023.
 */
interface DogFactsGateway {
    suspend fun getDogFacts(): DogFactsResponse
}
