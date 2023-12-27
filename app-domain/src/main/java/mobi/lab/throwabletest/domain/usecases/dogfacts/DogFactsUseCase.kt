package mobi.lab.throwabletest.domain.usecases.dogfacts

import kotlinx.coroutines.coroutineScope
import mobi.lab.throwabletest.domain.entities.dogfacts.DogFactsResponse
import mobi.lab.throwabletest.domain.gateway.DogFactsGateway
import mobi.lab.throwabletest.domain.usecases.UseCase
import javax.inject.Inject

/**
 * Created by siret on 27.12.2023.
 */
class DogFactsUseCase @Inject constructor(
    private val dogFactsGateway: DogFactsGateway,
) : UseCase() {

    suspend fun execute(): DogFactsResponse {
        return coroutineScope {
            return@coroutineScope dogFactsGateway.getDogFacts()
        }
    }
}
