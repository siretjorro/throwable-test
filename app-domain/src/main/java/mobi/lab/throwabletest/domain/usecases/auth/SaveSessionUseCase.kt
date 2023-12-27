package mobi.lab.throwabletest.domain.usecases.auth

import dagger.Reusable
import mobi.lab.throwabletest.domain.entities.Session
import mobi.lab.throwabletest.domain.storage.SessionStorage
import javax.inject.Inject

@Reusable
class SaveSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) {

    fun execute(session: Session) {
        sessionStorage.save(session)
    }
}
