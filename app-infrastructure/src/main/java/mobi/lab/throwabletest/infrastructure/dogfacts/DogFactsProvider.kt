package mobi.lab.throwabletest.infrastructure.dogfacts

import mobi.lab.throwabletest.domain.entities.dogfacts.DogFactsResponse
import mobi.lab.throwabletest.domain.gateway.DogFactsGateway
import mobi.lab.throwabletest.infrastructure.dogfacts.remote.DogFactsApi
import mobi.lab.throwabletest.infrastructure.dogfacts.remote.dto.ApiDogFactsResponse
import javax.inject.Inject

/**
 * Created by siret on 27.12.2023.
 */
internal class DogFactsProvider @Inject constructor(
    private val dogFactsApi: DogFactsApi
) : DogFactsGateway {

    override suspend fun getDogFacts(): DogFactsResponse {
        val response =  dogFactsApi.getDogFacts()
        return mapResponse(response)
    }

    private fun mapResponse(apiDogFactsResponse: ApiDogFactsResponse): DogFactsResponse {
        return DogFactsResponse(apiDogFactsResponse.facts, apiDogFactsResponse.success)
    }

}
