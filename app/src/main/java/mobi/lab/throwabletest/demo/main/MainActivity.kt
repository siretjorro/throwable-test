package mobi.lab.throwabletest.demo.main

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import mobi.lab.throwabletest.common.BaseFragmentActivity

class MainActivity : BaseFragmentActivity() {

    override val tag: String
        get() = MainFragment::class.java.name

    override fun createFragment(): Fragment {
        return MainFragment.newInstance()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
