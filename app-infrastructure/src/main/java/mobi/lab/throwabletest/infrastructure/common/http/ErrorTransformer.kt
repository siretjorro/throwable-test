package mobi.lab.throwabletest.infrastructure.common.http

interface ErrorTransformer {
    fun transform(error: Throwable): Throwable
}
