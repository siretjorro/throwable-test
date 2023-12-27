package mobi.lab.throwabletest.domain.gateway

import io.reactivex.rxjava3.core.Single
import mobi.lab.throwabletest.domain.entities.Session

interface AuthGateway {
    fun login(username: String, password: String): Single<Session>
}
