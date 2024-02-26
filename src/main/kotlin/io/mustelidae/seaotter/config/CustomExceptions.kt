package io.mustelidae.seaotter.config

open class HumanException(val errorSource: ErrorSource) : RuntimeException(errorSource.message)
class UnSupportException : HumanException(NormalError(ErrorCode.H001, ErrorCode.H001.description))

open class SystemException(val errorSource: ErrorSource) : RuntimeException()
