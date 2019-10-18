package io.mustelidae.seaotter.config

interface ErrorSource {
    fun getCode(): String
    fun getMessage(): String?
}
