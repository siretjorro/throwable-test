package mobi.lab.throwabletest.demo.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import mobi.lab.mvvm.assistedSavedStateViewModels
import mobi.lab.throwabletest.R
import mobi.lab.throwabletest.common.BaseFragment
import mobi.lab.throwabletest.common.FragmentBindingHolder
import mobi.lab.throwabletest.common.ViewBindingHolder
import mobi.lab.throwabletest.common.dialog.ConfirmationDialogFragment
import mobi.lab.throwabletest.common.dialog.ProgressDialogFragment
import mobi.lab.throwabletest.common.util.DialogUtil
import mobi.lab.throwabletest.common.util.formatErrorCode
import mobi.lab.throwabletest.databinding.DemoFragmentLoginBinding
import mobi.lab.throwabletest.demo.main.MainActivity
import mobi.lab.throwabletest.di.Injector
import mobi.lab.throwabletest.domain.entities.ErrorCode
import javax.inject.Inject

class LoginFragment : BaseFragment(), ViewBindingHolder<DemoFragmentLoginBinding> by FragmentBindingHolder() {

    @Inject lateinit var factory: LoginViewModel.Factory

    /**
     * Create the [LoginViewModel] via [LoginViewModel.Factory] by providing the [androidx.lifecycle.SavedStateHandle].
     * assistedSavedStateViewModels wraps a call to [androidx.lifecycle.AbstractSavedStateViewModelFactory] with this Fragment's
     * context and using the factory method here to create the actual ViewModel.
     *
     * Optionally, the assistedSavedStateViewModels takes a second argument in the form () -> Bundle?.
     * This can be used to provide an initial state for the [androidx.lifecycle.SavedStateHandle]
     * and is passed to [androidx.lifecycle.AbstractSavedStateViewModelFactory].
     */
    private val viewModel: LoginViewModel by assistedSavedStateViewModels { handle ->
        factory.create(handle)
    }

    init {
        Injector.inject(this)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(DemoFragmentLoginBinding.inflate(inflater), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding {
            buttonLogin.setOnClickListener { viewModel.onLoginClicked(getEmailString(), getPasswordString()) }
        }

        /**
         * Init ViewModel in onViewCreated as they are connected to View's lifecycle.
         */
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.action.onEachEvent { action ->
            val activity = activity
            if (activity == null) {
                return@onEachEvent false
            }

            when (action) {
                LoginViewModel.Action.OpenApplication -> {
                    startActivity(MainActivity.getIntent(activity))
                    activity.finishAffinity()
                }
            }
            return@onEachEvent true
        }
        viewModel.state.onEachNotNull { state ->
            when (state) {
                LoginViewModel.State.Default -> {
                    clearErrors()
                    hideProgress()
                }
                LoginViewModel.State.Progress -> {
                    clearErrors()
                    showProgress()
                }
                is LoginViewModel.State.Error -> {
                    hideProgress()
                    if (state.errorCode == ErrorCode.LOCAL_INVALID_CREDENTIALS) {
                        showInputErrors()
                    } else {
                        showLoginError(state.errorCode)
                    }
                }
            }
        }
    }

    private fun showInputErrors() {
        val binding = requireBinding()
        if (TextUtils.isEmpty(getEmailString())) {
            binding.inputLayoutEmail.error = getString(R.string.demo_text_required)
        }
        if (TextUtils.isEmpty(getPasswordString())) {
            binding.inputLayoutPassword.error = getString(R.string.demo_text_required)
        }
    }

    private fun showLoginError(error: ErrorCode) {
        DialogUtil.show(
            this,
            ConfirmationDialogFragment.newInstance(
                title = getString(R.string.demo_title_error),
                message = formatErrorCode(context, error),
                positiveButton = getString(android.R.string.ok)
            ),
            TAG_DIALOG_ERROR
        )
    }

    private fun clearErrors() {
        requireBinding {
            inputLayoutEmail.error = null
            inputLayoutPassword.error = null
        }
    }

    private fun getEmailString(): String {
        return requireBinding().editTextEmail.text.toString()
    }

    private fun getPasswordString(): String {
        return requireBinding().editTextPassword.text.toString()
    }

    private fun showProgress() {
        if (isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.show(this, ProgressDialogFragment.newInstance(), TAG_DIALOG_PROGRESS)
    }

    private fun hideProgress() {
        if (!isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.dismiss(this, TAG_DIALOG_PROGRESS)
    }

    private fun isProgressShown(): Boolean {
        return DialogUtil.isShowing(activity, TAG_DIALOG_PROGRESS)
    }

    companion object {
        const val TAG_DIALOG_PROGRESS = "login.TAG_DIALOG_PROGRESS"
        const val TAG_DIALOG_ERROR = "login.TAG_DIALOG_ERROR"

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
