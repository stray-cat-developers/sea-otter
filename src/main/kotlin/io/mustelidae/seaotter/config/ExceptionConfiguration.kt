package io.mustelidae.seaotter.config

import com.amazonaws.AmazonClientException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import javax.servlet.http.HttpServletRequest

@ControllerAdvice(annotations = [RestController::class])
class ExceptionConfiguration
@Autowired constructor(
    private val env: Environment
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [
        RuntimeException::class,
        IllegalStateException::class
    ])
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGlobalException(e: RuntimeException, request: HttpServletRequest): Map<String, Any> {
        log.error("Unexpected error", e)
        return errorForm(request, AppError(ErrorCode.S000, ErrorCode.S000.description))
    }

    @ExceptionHandler(value = [SystemException::class])
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleSystemException(e: SystemException, request: HttpServletRequest): Map<String, Any> {
        return errorForm(request, e.errorSource)
    }

    @ExceptionHandler(value = [AmazonClientException::class])
    fun handleAWSException(e: AmazonClientException, request: HttpServletRequest): Map<String, Any> {
        log.error("aws communication fail", e)
        return errorForm(request, AppError(ErrorCode.S002, ErrorCode.S002.description))
    }

    @ExceptionHandler(value = [HumanException::class])
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleHumanException(e: HumanException, request: HttpServletRequest): Map<String, Any> {
        return errorForm(request, e.errorSource)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: HttpServletRequest): Map<String, Any> {
        log.error("[T] wrong input.", e)
        return errorForm(request, AppError(ErrorCode.H002, ErrorCode.H002.description))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun methodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): MutableMap<String, Any> {

        val error = errorForm(request, AppError(ErrorCode.H002, ErrorCode.H002.description))

        error["errors"]?.let {
            @Suppress("UNCHECKED_CAST")
            error["errors"] = methodArgumentNotValidExceptionErrorForm(it as List<FieldError>)
        }

        return error
    }

    private fun errorForm(request: HttpServletRequest, errorSource: ErrorSource): MutableMap<String, Any> {
        val errorAttributes =
            DefaultErrorAttributes().getErrorAttributes(ServletWebRequest(request), isDevelopMode(env))

        errorAttributes["status"] = "fail"
        errorAttributes["code"] = errorSource.getCode()
        errorAttributes["message"] = errorSource.getMessage()

        return errorAttributes
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
        "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
    )
    private fun methodArgumentNotValidExceptionErrorForm(errors: List<FieldError>) =
            errors.map {
                    ValidationError(field = it.field,
                        rejectedValue = it.rejectedValue.toString(),
                        message = it.defaultMessage)
                }.toList()

    private data class ValidationError(
        val field: String,
        val rejectedValue: String,
        val message: String
    )
}

private fun isDevelopMode(env: Environment): Boolean {
    val profiles = env.activeProfiles

    return (profiles.contains("default"))
}
