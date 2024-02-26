package io.mustelidae.seaotter.config

import com.amazonaws.AmazonClientException
import io.mustelidae.seaotter.utils.Jackson
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.error.ErrorAttributeOptions
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

@ControllerAdvice(annotations = [RestController::class])
class ExceptionConfiguration
@Autowired constructor(
    private val env: Environment,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(
        value = [
            RuntimeException::class,
            IllegalStateException::class,
        ],
    )
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGlobalException(e: RuntimeException, request: HttpServletRequest): GlobalErrorFormat {
        log.error("Unexpected error", e)
        return errorForm(request, AppError(ErrorCode.S000, ErrorCode.S000.description), e)
    }

    @ExceptionHandler(value = [SystemException::class])
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleSystemException(e: SystemException, request: HttpServletRequest): GlobalErrorFormat {
        return errorForm(request, e.errorSource, e)
    }

    @ExceptionHandler(value = [AmazonClientException::class])
    fun handleAWSException(e: AmazonClientException, request: HttpServletRequest): GlobalErrorFormat {
        log.error("aws communication fail", e)
        return errorForm(request, AppError(ErrorCode.S002, ErrorCode.S002.description), e)
    }

    @ExceptionHandler(value = [HumanException::class])
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleHumanException(e: HumanException, request: HttpServletRequest): GlobalErrorFormat {
        return errorForm(request, e.errorSource, e)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: HttpServletRequest): GlobalErrorFormat {
        log.error("[T] wrong input.", e)
        return errorForm(request, AppError(ErrorCode.H002, ErrorCode.H002.description), e)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun methodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): GlobalErrorFormat {
        val message = methodArgumentNotValidExceptionErrorForm(e.bindingResult.fieldErrors)
            .joinToString(",") { "reject field(${it.field}) and value(${it.rejectedValue}" }

        return errorForm(request, AppError(ErrorCode.H002, message), e)
    }

    private fun errorForm(request: HttpServletRequest, errorSource: ErrorSource, e: Exception): GlobalErrorFormat {
        val errorAttributeOptions = if (env.activeProfiles.contains("prod").not()) {
            ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)
        } else {
            ErrorAttributeOptions.defaults()
        }

        val errorAttributes =
            DefaultErrorAttributes().getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)

        errorAttributes["code"] = errorSource.getCode()
        errorAttributes["message"] = errorSource.getMessage()

        errorAttributes.apply {
            this["message"] = errorSource.getMessage()
            this["code"] = errorSource.getCode()
            this["type"] = e.javaClass.simpleName
        }

        return Jackson.getMapper().convertValue(errorAttributes, GlobalErrorFormat::class.java)
    }

    private fun methodArgumentNotValidExceptionErrorForm(errors: List<FieldError>) =
        errors.map {
            ValidationError(
                field = it.field,
                rejectedValue = it.rejectedValue.toString(),
                message = it.defaultMessage ?: "validate fail.",
            )
        }.toList()

    private data class ValidationError(
        val field: String,
        val rejectedValue: String,
        val message: String,
    )
}
