package mobi.lab.throwabletest.domain.usecases.auth

import dagger.Reusable
import mobi.lab.throwabletest.domain.storage.SessionStorage
import javax.inject.Inject

@Reusable
class DeleteSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) {

    fun execute() {
        sessionStorage.clear()
    }
}
