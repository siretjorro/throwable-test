package mobi.lab.throwabletest.domain.usecases.auth

import dagger.Reusable
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@Reusable
class LogoutUseCase @Inject constructor(
    private val deleteSessionUseCase: DeleteSessionUseCase
) {
    fun execute(): Completable {
        return Completable.fromCallable { deleteSessionUseCase.execute() }
    }
}
