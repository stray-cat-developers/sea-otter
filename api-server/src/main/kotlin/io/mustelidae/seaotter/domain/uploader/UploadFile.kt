package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.constant.ImageFileFormat
import org.bson.types.ObjectId
import java.awt.image.BufferedImage

data class UploadFile(
    val bufferedImage: BufferedImage,
    val fileFormat: ImageFileFormat,
    val isUnRetouched: Boolean,
    val name: String = ObjectId().toString()
)
