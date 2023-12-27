package mobi.lab.throwabletest.demo.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mobi.lab.mvvm.SingleEvent
import mobi.lab.mvvm.asLiveData
import mobi.lab.throwabletest.common.coroutine.withContextCatching
import mobi.lab.throwabletest.common.rx.SchedulerProvider
import mobi.lab.throwabletest.common.rx.dispose
import mobi.lab.throwabletest.domain.entities.dogfacts.DogFactsResponse
import mobi.lab.throwabletest.domain.usecases.auth.LogoutUseCase
import mobi.lab.throwabletest.domain.usecases.dogfacts.DogFactsUseCase
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val dogFactsUseCase: DogFactsUseCase,
    private val schedulers: SchedulerProvider
) : ViewModel() {

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private val _state: MutableLiveData<State> = MutableLiveData(State.Default)
    val state = _state.asLiveData()

    private var disposable: Disposable? = null

    init {
        getDogFacts()
    }

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
    }

    fun onDebugClicked() {
        _action.value = SingleEvent(Action.OpenDebug())
    }

    fun onLogoutClicked() {
        dispose(disposable)
        disposable = logoutUseCase.execute()
            .compose(schedulers.completable())
            .subscribe(
                { _action.value = SingleEvent(Action.RestartApplication) }, // Success
                { // Error
                    Timber.e(it, "logout error")
                    _action.value = SingleEvent(Action.RestartApplication)
                }
            )
    }

    fun getDogFacts() {
        _state.value = State.Loading
        viewModelScope.launch {
            withContextCatching(Dispatchers.IO) {
                dogFactsUseCase.execute()
            }.onSuccess {
                onGetDogFactsSuccess(it)
            }.onFailure {
                onGetDogFactsFailure(it)
            }
        }
    }

    private fun onGetDogFactsSuccess(response: DogFactsResponse) {
        _state.value = State.Default
        _action.value = SingleEvent(Action.ShowDogFact(response.facts))
    }

    private fun onGetDogFactsFailure(error: Throwable) {
        _state.value = State.Error(error)
    }

    sealed class Action {
        object RestartApplication : Action()
        class OpenDebug : Action()
        data class ShowDogFact(val facts: List<String>) : Action()
    }

    sealed class State {
        object Default : State()
        object Loading : State()
        data class Error(val error: Throwable) : State()
    }
}
