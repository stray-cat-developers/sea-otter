package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.utils.sha1
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DirectoryPath(
    base: String,
    shardType: String?,
    topicCode: String? = null,
) {
    private var path: String = if (topicCode == null) base else "$base/topic/$topicCode"

    fun append(isOriginal: Boolean) {
        path += if (isOriginal) "/untouched" else "/edited"
    }

    fun append(path: String) {
        this.path += if (path.first() == '/') path else "/$path"
    }

    fun appendImageName(name: String, format: ImageFileFormat) {
        this.path += "/$name.${format.name.lowercase(Locale.getDefault())}"
    }

    fun appendFileName(name: String) {
        this.path += "/$name"
    }

    fun getPath(): String {
        return path.replace("//", "/")
    }

    init {
        when (shardType) {
            "date" -> {
                path += "/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            }
            "sha1" -> {
                path += "/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE).sha1()
            }
            else -> {}
        }
    }
}
