package io.mustelidae.seaotter.config

interface ErrorSource {
    val code: String
    val message: String?
    var causeBy: Map<String, Any?>?
    var refCode: String?
}
