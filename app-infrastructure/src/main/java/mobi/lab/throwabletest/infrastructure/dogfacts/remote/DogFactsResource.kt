package mobi.lab.throwabletest.infrastructure.dogfacts.remote

import mobi.lab.throwabletest.infrastructure.dogfacts.remote.dto.ApiDogFactsResponse
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by siret on 27.12.2023.
 */
internal interface DogFactsResource {
    @GET
    suspend fun getDogFacts(@Url url: String): ApiDogFactsResponse
}
