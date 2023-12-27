package mobi.lab.throwabletest.di

import android.app.Application
import mobi.lab.throwabletest.App
import mobi.lab.throwabletest.demo.login.LoginFragment
import mobi.lab.throwabletest.demo.main.MainFragment
import mobi.lab.throwabletest.splash.SplashActivity

/**
 * Convenience wrapper to manage Dagger graph creation and components. Can be used to control the lifecycle of additional components if needed.
 *
 * NB! UseCaseModule uses constructor injection for providing UseCases. Gateway and Storage modules are only used by UseCaseModule.
 * Since Dagger can resolve UseCaseModule on its own, specifically providing said modules is deprecated (a no-op in reality).
 */
object Injector : BaseAppComponent {

    private lateinit var appComponent: BaseAppComponent

    /**
     * Convenience function to init the dependency graph from the given [Application] instance.
     * Automatically creates the default [AppComponent] with required modules and saves it as our internal [BaseAppComponent] instance.
     *
     * @param application [Application] instance
     */
    fun buildGraph(application: Application) {
        buildGraph(
            DaggerAppComponent
                .builder()
                .appModule(AppModule(application))
                .build()
        )
    }

    /**
     * Initialize [Injector] with an instance of [BaseAppComponent] that is the main entry point to inject dependencies
     * into this application. Prefer calling the [buildGraph] with [Application] for most usages.
     * This function is useful when creating an alternative BaseAppComponent implementation for tests or custom environments.
     *
     * @param component [BaseAppComponent] instance
     */
    fun buildGraph(component: BaseAppComponent) {
        appComponent = component
    }

    override fun inject(target: App) {
        appComponent.inject(target)
    }

    override fun inject(target: SplashActivity) {
        appComponent.inject(target)
    }

    override fun inject(target: MainFragment) {
        appComponent.inject(target)
    }

    override fun inject(target: LoginFragment) {
        appComponent.inject(target)
    }
}
