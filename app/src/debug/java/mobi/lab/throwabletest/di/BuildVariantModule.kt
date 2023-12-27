package mobi.lab.throwabletest.di

import dagger.Module
import dagger.Provides
import mobi.lab.throwabletest.common.debug.DebugActions
import mobi.lab.throwabletest.debug.DevDebugActions
import javax.inject.Singleton

@Module
object BuildVariantModule {

    @Provides
    @Singleton
    fun provideDebugActions(): DebugActions {
        return DevDebugActions()
    }
}
