package mobi.lab.throwabletest.common.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import mobi.lab.throwabletest.splash.SplashActivity
import timber.log.Timber

object NavUtil {

    fun openBrowser(context: Context, url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (error: ActivityNotFoundException) {
            Timber.e(error, "openWebUrl")
        }
    }

    fun restartApplication(context: Context) {
        val intent = Intent(context, SplashActivity::class.java)
        val restartIntent = Intent.makeRestartActivityTask(intent.component)
        context.startActivity(restartIntent)
    }
}
