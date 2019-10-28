package io.mustelidae.seaotter.config

open class HumanException(val errorSource: ErrorSource) : RuntimeException(errorSource.getMessage())
class UnSupportException : HumanException(AppError(ErrorCode.H001, ErrorCode.H001.description))

open class SystemException(val errorSource: ErrorSource) : RuntimeException()
