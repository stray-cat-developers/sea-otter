package io.mustelidae.seaotter.utils

import com.google.common.io.Files
import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.constant.ImageFileFormat
import org.springframework.web.multipart.MultipartFile
import java.security.MessageDigest
import java.util.Locale

const val HEX_CHARS = "239DA12B7590A0AC"

fun String.sha1(): String {
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

fun MultipartFile.extension(): ImageFileFormat {
    val extension = Files.getFileExtension(this.originalFilename!!)
    return try {
        ImageFileFormat.valueOf(extension.uppercase(Locale.getDefault()))
    } catch (e: IllegalArgumentException) {
        throw UnSupportException()
    }
}

fun MultipartFile.isSupport(): Boolean {
    return try {
        this.extension().support
    } catch (e: UnSupportException) {
        false
    }
}
