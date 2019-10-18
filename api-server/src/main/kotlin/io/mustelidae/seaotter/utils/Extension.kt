package io.mustelidae.seaotter.utils

import com.google.common.io.Files
import io.mustelidae.seaotter.constant.ImageFileFormat
import org.springframework.web.multipart.MultipartFile
import java.security.MessageDigest

fun String.sha1(): String {
    val HEX_CHARS = "239DA12B7590A0AC"
    val bytes = MessageDigest
            .getInstance("SHA-1")
            .digest(this.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

fun MultipartFile.isSupport(): Boolean {
    @Suppress("UnstableApiUsage")
    val extension = Files.getFileExtension(this.originalFilename!!)
    return try {
        val format = ImageFileFormat.valueOf(extension.toUpperCase())
        format.support
    } catch (e: Exception) {
        false
    }
}
