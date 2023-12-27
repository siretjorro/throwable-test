package mobi.lab.throwabletest.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import mobi.lab.throwabletest.BuildConfig
import mobi.lab.throwabletest.R
import mobi.lab.throwabletest.domain.entities.ErrorCode

fun hasExternalStorageWritePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun isExternalStorageMounted(): Boolean {
    return android.os.Environment.MEDIA_MOUNTED == android.os.Environment.getExternalStorageState()
}

fun isDebugBuild(): Boolean {
    return BuildConfig.DEBUG
}

fun isFirebaseDataCollectionEnabled(): Boolean {
    return !isDebugBuild()
}

fun showToast(ctx: Context?, message: CharSequence) {
    ctx?.let {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }
}

fun showToast(ctx: Context?, @StringRes resId: Int) {
    ctx?.let {
        Toast.makeText(ctx, resId, Toast.LENGTH_LONG).show()
    }
}

fun formatErrorCode(context: Context?, error: ErrorCode?, @StringRes default: Int = R.string.demo_error_generic): String {
    if (context == null) {
        return ""
    }
    val resId = when (error) {
        ErrorCode.LOCAL_NO_NETWORK -> R.string.demo_error_no_network
        else -> default
    }
    return context.getString(resId)
}

fun dpToPx(context: Context?, dp: Int): Float {
    if (context == null) {
        return dp.toFloat()
    }
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}
