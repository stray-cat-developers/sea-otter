package io.mustelidae.seaotter.config

class NormalError(errorCode: ErrorCode, message: String? = null, override var causeBy: Map<String, Any?>? = null) : ErrorSource {
    override val message: String = message ?: errorCode.description
    override val code: String = errorCode.name
    override var refCode: String? = null
}
