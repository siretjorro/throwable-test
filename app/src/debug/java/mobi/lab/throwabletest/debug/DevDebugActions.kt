package mobi.lab.throwabletest.debug

import android.content.Context
import mobi.lab.throwabletest.common.debug.DebugActions

class DevDebugActions : DebugActions {
    override fun launchDebugActivity(context: Context) {
        return context.startActivity(DebugActivity.getIntent(context))
    }
}
