package io.mustelidae.seaotter.config

open class HumanException(val errorSource: ErrorSource) : RuntimeException(errorSource.getMessage())
class DataNotFoundException(message: String) : HumanException(AppError(ErrorCode.H003, message))
class UnSupportException : HumanException(AppError(ErrorCode.H001, ErrorCode.H001.description))

open class SystemException(val errorSource: ErrorSource) : RuntimeException()
class DevelopMistakeException(message: String) : SystemException(AppError(ErrorCode.S001, message))
