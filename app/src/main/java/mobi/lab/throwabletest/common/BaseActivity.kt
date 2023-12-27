package mobi.lab.throwabletest.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import mobi.lab.mvvm.MvvmLiveDataExtensions
import timber.log.Timber

open class BaseActivity : AppCompatActivity, MvvmLiveDataExtensions {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    /**
     * Activity Lifecycle is also connected to it's View lifecycle so we want to return
     * the lifecycle of the Activity itself here. See [BaseMvvmFragment] for why you'd want something different.
     */
    override fun getLifecycleOwner(): LifecycleOwner = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate this=$this")
    }

    override fun onStart() {
        super.onStart()
        log("onStart this=$this")
    }

    override fun onResume() {
        super.onResume()
        log("onResume this=$this")
    }

    override fun onPause() {
        super.onPause()
        log("onPause this=$this")
    }

    override fun onStop() {
        super.onStop()
        log("onStop this=$this")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy this=$this")
    }

    private fun log(message: String) {
        Timber.v(message)
    }
}
