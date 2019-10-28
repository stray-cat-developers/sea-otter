package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.utils.sha1
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DirectoryPath(
    private val base: String,
    private val shard: String?
) {
    private var path: String = base
    private var shardType: String? = shard

    fun init() {
        path = base
        shardType = shard
    }

    fun append(isOriginal: Boolean) {
        path += if (isOriginal) "/untouched" else "/edited"
    }

    fun append(path: String) {
        this.path += if (path.first() == '/') path else "/$path"
    }

    fun append(name: String, format: ImageFileFormat) {
        this.path += "/$name.${format.name.toLowerCase()}"
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
