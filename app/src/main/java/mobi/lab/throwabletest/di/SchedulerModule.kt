package mobi.lab.throwabletest.di

import dagger.Module
import dagger.Provides
import mobi.lab.throwabletest.common.rx.AndroidSchedulerProvider
import mobi.lab.throwabletest.common.rx.SchedulerProvider
import javax.inject.Singleton

/**
 * SchedulerProvider is in a separate module to make it easier to provide alternative dependencies as needed.
 * For example, instrumentation (ui) tests are a good example where a different implementation for this is necessary.
 */
@Module
object SchedulerModule {
    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()
}
