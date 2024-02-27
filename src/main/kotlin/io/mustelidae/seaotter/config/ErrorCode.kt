package io.mustelidae.seaotter.config

enum class ErrorCode(val description: String) {
    // human error
    /* common */
    H001("Unsupported image format"),

    /* wrong input */
    H002("Invalid input"),

    // system error
    S000("Unknown internal server error"),
    S001("Develop mistake"),
    S002("Server to server communication fail"),
}
