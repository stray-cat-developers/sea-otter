package io.mustelidae.seaotter.domain.uploader

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Directory Base Path Generator
 * Simply set the default path.
 * If there are more files, fix them here. And let's do sharding.
 */
@Deprecated("remove")
object PathGenerator {

    fun getPathByDate(prefixPath: String): String {
        val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return "$date/$prefixPath".replace("//", "/")
    }
}
