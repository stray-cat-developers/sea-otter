package io.mustelidae.seaotter.utils

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.result.Result
import org.springframework.http.HttpStatus

fun ResponseResultOf<String>.success(): Result<String, FuelError> {
    val (req, res, result) = this
    if (HttpStatus.valueOf(res.statusCode).is2xxSuccessful.not()) {
        throw IllegalStateException("http state fail.")
    }

    return result
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
