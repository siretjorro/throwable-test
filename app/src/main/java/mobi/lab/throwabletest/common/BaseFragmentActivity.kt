package mobi.lab.throwabletest.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import mobi.lab.throwabletest.R

abstract class BaseFragmentActivity : BaseActivity {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected abstract val tag: String
    protected abstract fun createFragment(): Fragment?

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_base_fragment)
        if (savedInstanceState == null) {
            var fragment = supportFragmentManager.findFragmentByTag(tag)
            if (fragment == null) {
                fragment = createFragment()
            }
            if (fragment != null) {
                showFragment(fragment, tag)
            }
        }
    }

    protected fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}
