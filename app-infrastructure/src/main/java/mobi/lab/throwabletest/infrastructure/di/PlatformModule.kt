package mobi.lab.throwabletest.infrastructure.di

import android.content.Context
import dagger.Module
import dagger.Provides
import mobi.lab.throwabletest.infrastructure.common.http.ErrorTransformer
import mobi.lab.throwabletest.infrastructure.common.http.MyErrorTransformer
import mobi.lab.throwabletest.infrastructure.common.json.Json
import mobi.lab.throwabletest.infrastructure.common.json.MoshiFactory
import mobi.lab.throwabletest.infrastructure.common.json.MoshiJson
import mobi.lab.throwabletest.infrastructure.common.platform.NetworkMonitor
import mobi.lab.throwabletest.infrastructure.common.remote.error.ApiErrorResponseMapper
import javax.inject.Singleton

@Module
object PlatformModule {

    @Provides
    @Singleton
    internal fun provideJson(): Json = MoshiJson(MoshiFactory.get())

    @Provides
    @Singleton
    internal fun provideNetworkMonitor(context: Context): NetworkMonitor = NetworkMonitor(context)

    @Provides
    @Singleton
    internal fun provideErrorTransformer(networkMonitor: NetworkMonitor, errorMapper: ApiErrorResponseMapper, json: Json): ErrorTransformer {
        return MyErrorTransformer(networkMonitor, errorMapper, json)
    }
}
