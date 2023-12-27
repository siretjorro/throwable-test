package mobi.lab.throwabletest.infrastructure.di

import dagger.Binds
import dagger.Module
import mobi.lab.throwabletest.domain.gateway.AuthGateway
import mobi.lab.throwabletest.domain.gateway.DogFactsGateway
import mobi.lab.throwabletest.infrastructure.auth.AuthProvider
import mobi.lab.throwabletest.infrastructure.dogfacts.DogFactsProvider

@Module(includes = [GatewayModule.Definitions::class])
object GatewayModule {

    @Module
    internal interface Definitions {
        @Binds fun bindAuthGateway(impl: AuthProvider): AuthGateway
        @Binds fun bindDogFactsGateway(impl: DogFactsProvider): DogFactsGateway
    }
}
