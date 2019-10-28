package io.mustelidae.seaotter.utils

import com.github.kittinunf.fuel.util.encodeBase64
import com.google.common.io.Files
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.common.Reply
import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.constant.ImageFileFormat
import org.springframework.util.Base64Utils
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

fun MultipartFile.extension(): ImageFileFormat {
    @Suppress("UnstableApiUsage")
    val extension = Files.getFileExtension(this.originalFilename!!)
    return try {
        ImageFileFormat.valueOf(extension.toUpperCase())
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

fun <T> List<T>.toReplies(): Replies<T> = Replies(this)
fun <T> T.toReply(): Reply<T> = Reply(this)

fun String.decodeFromUrlSafe(): String {
    val index = this.lastIndexOf(',')
    val base64SafeUrl = this.substring(index + 1)
    val header = this.substring(0, index).replace('_', '/')

    val byteArray = Base64Utils.decodeFromUrlSafeString(base64SafeUrl)
    val encodedBase64 = String(byteArray.encodeBase64())

    return "$header,$encodedBase64"
}
