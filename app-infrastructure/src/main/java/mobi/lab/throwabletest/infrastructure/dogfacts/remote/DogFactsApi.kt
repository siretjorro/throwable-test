package mobi.lab.throwabletest.infrastructure.dogfacts.remote

import mobi.lab.throwabletest.infrastructure.common.remote.WrappedApiClient
import mobi.lab.throwabletest.infrastructure.dogfacts.remote.dto.ApiDogFactsResponse
import javax.inject.Inject

/**
 * Created by siret on 27.12.2023.
 */
internal class DogFactsApi @Inject constructor(
    private val wrapper: WrappedApiClient,
    private val res: DogFactsResource
) {
    suspend fun getDogFacts(): ApiDogFactsResponse {
        return wrapper.wrap { res.getDogFacts("https://dog-api.kinduff.com/api/facts") }
    }
}
