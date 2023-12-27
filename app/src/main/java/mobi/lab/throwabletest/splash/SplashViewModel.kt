package mobi.lab.throwabletest.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import mobi.lab.mvvm.SingleEvent
import mobi.lab.mvvm.asLiveData
import mobi.lab.throwabletest.common.rx.SchedulerProvider
import mobi.lab.throwabletest.common.rx.dispose
import mobi.lab.throwabletest.domain.usecases.auth.HasValidSessionUseCase
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: HasValidSessionUseCase,
    private val schedulers: SchedulerProvider,
) : ViewModel() {

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private var disposable: Disposable? = null

    init {
        checkSession()
    }

    private fun checkSession() {
        dispose(disposable)
        disposable = checkSessionUseCase.execute()
            .compose(schedulers.completableNoAuthCheck())
            .subscribe(
                { _action.value = SingleEvent(Action.LaunchApplication) },
                { error ->
                    Timber.e(error, "checkSession")
                    _action.value = SingleEvent(Action.LaunchLogin)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
    }

    sealed class Action {
        object LaunchApplication : Action()
        object LaunchLogin : Action()
    }
}
