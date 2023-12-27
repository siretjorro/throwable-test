package mobi.lab.throwabletest

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.os.Process
import android.os.StrictMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import mobi.lab.throwabletest.common.logging.CrashlyticsTree
import mobi.lab.throwabletest.common.logging.ScrollsTree
import mobi.lab.throwabletest.common.platform.LogoutMonitor
import mobi.lab.throwabletest.common.platform.MyNotificationManager
import mobi.lab.throwabletest.common.util.isDebugBuild
import mobi.lab.throwabletest.common.util.isFirebaseDataCollectionEnabled
import mobi.lab.throwabletest.di.Injector
import mobi.lab.throwabletest.domain.storage.SessionStorage
import mobi.lab.throwabletest.domain.usecases.auth.DeleteSessionUseCase
import mobi.lab.throwabletest.infrastructure.common.platform.AppEnvironment
import mobi.lab.throwabletest.infrastructure.common.platform.ImageLoader
import mobi.lab.scrolls.LogPost
import mobi.lab.scrolls.LogPostBuilder
import mobi.lab.scrolls.LogPostImpl
import timber.log.Timber
import javax.inject.Inject

open class App : Application() {

    @Inject lateinit var environment: AppEnvironment
    @Inject lateinit var sessionStorage: SessionStorage
    @Inject lateinit var deleteSessionUseCase: DeleteSessionUseCase

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()

        initLogPost()
        if (isLogPostProcess()) {
            android.util.Log.d("LogPost", "Halting app onCreate as we are in logpost")
            return
        }

        initRxJavaErrorHandler()
        initLogPostHandler()
        initLogging()
        initFirebase()
        initStrictMode()
        initDependencyInjection()
        Injector.inject(this)

        LogoutMonitor.init(this, deleteSessionUseCase)
        MyNotificationManager.newInstance(this).createChannels()
        ImageLoader.configure(this, environment, sessionStorage)
    }

    protected open fun initDependencyInjection() {
        Injector.buildGraph(this)
    }

    private fun initRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler { error ->
            Timber.e(error, "Uncaught exception")
            FirebaseCrashlytics.getInstance().recordException(error)
            if (isDebugBuild()) {
                throw error
            }
        }
    }

    private fun initFirebase() {
        /*
         * Set up Firebase Crashlytics and Analytics, disabled for debug builds.
         * Crashlytics and Analytics are disabled by default by AndroidManifest.xml metadata tags.
         * Collection can be enabled at runtime on the app side if needed. For example, after asking for user consent, etc.
         * But by default, the collection is disabled on the Manifest level until it is enabled here in the App onCreate call.
         */
        val enabled = isFirebaseDataCollectionEnabled()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enabled)
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(enabled)
    }

    private fun initLogging() {
        var crashlyticsLogLevel = android.util.Log.WARN
        if (isDebugBuild()) {
            Timber.plant(ScrollsTree(this))
            crashlyticsLogLevel = android.util.Log.VERBOSE
        }
        Timber.plant(CrashlyticsTree(crashlyticsLogLevel))
    }

    private fun initLogPostHandler() {
        if (!isDebugBuild()) {
            return
        }

        val defaultExceptionHandler = Thread.currentThread().uncaughtExceptionHandler
        Thread.currentThread().uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, ex ->
            // Do the things we need to do
            Timber.e(ex, "FATAL EXCEPTION")

            LogPostBuilder()
                .setConfirmEnabled(true)
                .setShowResultEnabled(true)
                .addTags(LogPost.LOG_TAG_CRASH)
                .launchActivity(applicationContext)

            // Call the system default handler
            defaultExceptionHandler?.uncaughtException(thread, ex)
        }
    }

    private fun initLogPost() {
        if (!isDebugBuild()) {
            return
        }

        /* Configure posting to email */
        LogPostImpl.configure(this, BuildConfig.APPLICATION_ID + ".logs")
    }

    private fun isLogPostProcess(): Boolean {
        if (!isDebugBuild()) {
            return false
        }
        for (inf in (getSystemService(ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses) {
            if (inf.pid == Process.myPid()) {
                if (".LogPostProcess" == inf.processName) {
                    return true
                }
            }
        }
        return false
    }

    private fun initStrictMode() {
        if (isDebugBuild()) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    // Use this if actually looking for something
                    // .penaltyDeath()
                    .build()
            )
        }
    }
}
