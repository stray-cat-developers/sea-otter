package io.mustelidae.seaotter.utils

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.result.Result
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
class HttpHelper {

    companion object {
        private val log = LoggerFactory.getLogger(HttpHelper::class.java)

        fun httpStatusCheck(req: Request, res: Response, result: Result<String, FuelError>) {
            log.info("$req\n-------\n$res")

            val (content, error) = result
            if (HttpStatus.valueOf(res.statusCode).is2xxSuccessful.not()) {
                error?.let { log.error(error.toString()) }
                throw IllegalStateException("http state fail.")
            }

            log.debug("response content: $content")
        }

        fun getHttpFailError(req: Request, res: Response, result: Result<String, FuelError>): HttpFail {
            log.info("$req\n-------\n$res")
            val (_, error) = result
            if (HttpStatus.valueOf(res.statusCode).is2xxSuccessful) {
                throw IllegalStateException("http state is not fail")
            }

            val json = String(error!!.errorData)
            log.debug(json)
            return Jackson.getMapper().readValue(json, HttpFail::class.java)
        }
    }
}

fun ResponseResultOf<String>.success(): Result<String, FuelError> {
    val (_, res, result) = this
    if (HttpStatus.valueOf(res.statusCode).is2xxSuccessful.not()) {
        throw IllegalStateException("http state fail.")
    }

    return result
}

class HttpFail(
    val code: String,
    val status: String,
    val message: String?,
    val timestamp: String
)
internal inline fun <reified T> Result<String, FuelError>.fromJson(): T {
    return this.component1()!!
        .fromJson()
}

internal inline fun <reified T> Result<String, FuelError>.fromJsonByContent(): T {
    return this.component1()!!
        .fromJsonByContent()
}
internal fun <T> T.toJson(): String = Jackson.getMapper().writeValueAsString(this)

inline fun <reified T> String.fromJson(): T = Jackson.getMapper().readValue(this)
inline fun <reified T> String.fromJsonByContent(): T {
    val mapper = Jackson.getMapper()
    val content = mapper.readTree(this).get("content").toString()
    return mapper.readValue(content)
}
