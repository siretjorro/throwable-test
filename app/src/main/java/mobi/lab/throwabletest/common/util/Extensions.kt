package mobi.lab.throwabletest.common.util

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import mobi.lab.throwabletest.R
import mobi.lab.throwabletest.domain.entities.DomainException
import mobi.lab.throwabletest.domain.entities.ErrorCode
import timber.log.Timber

/**
 * Since isVisible can't be overridden, then we need to implement our own
 * If a nested fragment's parent fragment is hidden, then nested fragment's visibility does not change.
 * To fix that, we need to look at all the parent fragments and check their visibility.
 */
fun Fragment.isFragmentVisible(): Boolean {
    Timber.d("isFragmentVisible fragment=$this visible=$isVisible")
    val parent = parentFragment
    if (parent != null) {
        return isVisible && parent.isFragmentVisible()
    }
    return isVisible
}

fun SpannableString.addSpan(spannablePart: String, span: Any): SpannableString {
    val start = this.indexOf(spannablePart)
    val end = start + spannablePart.length
    if (start > -1) {
        setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun Throwable.errorCode(): ErrorCode {
    return if (this is DomainException) code else ErrorCode.UNKNOWN
}

fun ErrorCode.formatErrorCode(context: Context?, @StringRes default: Int = R.string.demo_error_generic): String {
    return formatErrorCode(context, this, default)
}
