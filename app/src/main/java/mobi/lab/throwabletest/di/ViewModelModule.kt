package mobi.lab.throwabletest.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import mobi.lab.throwabletest.demo.main.MainViewModel
import mobi.lab.throwabletest.di.annotations.ViewModelKey
import mobi.lab.throwabletest.splash.SplashViewModel

@Module(includes = [ViewModelModule.Definitions::class])
object ViewModelModule {

    @Module
    internal interface Definitions {
        @Binds
        @IntoMap
        @ViewModelKey(SplashViewModel::class)
        fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        fun bindMainViewModel(viewModel: MainViewModel): ViewModel
    }
}
