package io.mustelidae.seaotter.config

class AppError(
    private val errorCode: ErrorCode,
    private val message: String,
) : ErrorSource {
    override fun getCode(): String = errorCode.name
    override fun getMessage(): String? = message
}
