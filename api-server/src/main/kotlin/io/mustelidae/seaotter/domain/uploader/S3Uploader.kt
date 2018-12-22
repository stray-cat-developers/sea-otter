package io.mustelidae.seaotter.domain.uploader

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.mustelidae.seaotter.config.OtterEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import org.bson.types.ObjectId
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Upload images to google s3.
 * If the use of the cloud front is enabled, make url the cloud front url.
 */
internal class S3Uploader(
    private val awsS3: OtterEnvironment.AwsS3
) : Uploader {

    private lateinit var prefixPath: String
    private lateinit var imageFileFormat: ImageFileFormat
    private lateinit var fileName: String

    private val s3Client = AmazonS3ClientBuilder.defaultClient()

    /**
     * Define the file you want to save.
     */
    fun defineFile(imageFileFormat: ImageFileFormat, name: String = ObjectId().toString()) {
        this.imageFileFormat = imageFileFormat
        this.fileName = "$name.${imageFileFormat.name.toLowerCase()}"
    }

    /**
     * Set storage location.
     */
    fun definePath(path: String) {
        prefixPath = path
    }

    fun defineEditPath() {
        prefixPath = awsS3.path.editedPath
    }

    fun defineUnRetouchedPath() {
        prefixPath = awsS3.path.unRetouchedPath
    }

    override fun upload(bytes: ByteArray): String {
        val metaData = ObjectMetadata().apply {
            contentLength = bytes.size.toLong()
            contentType = "image/${imageFileFormat.name.toLowerCase()}"
        }
        val pathAndFileName = "$prefixPath/$fileName"
        val putObjectRequest = PutObjectRequest(awsS3.bucket, pathAndFileName, ByteArrayInputStream(bytes), metaData).apply {
            withCannedAcl(CannedAccessControlList.PublicRead)
        }

        s3Client.putObject(putObjectRequest)

        return pathAndFileName
    }

    override fun upload(bufferedImage: BufferedImage): String {
        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageFileFormat.name.toLowerCase(), out)
        return upload(out.toByteArray())
    }

    override fun makeFullUrl(pathAndFileName: String): String {
        return if (awsS3.cloudFront.enabled) {
            "${awsS3.cloudFront.url}/$pathAndFileName"
        } else {
            s3Client.getUrl(awsS3.bucket, pathAndFileName).toString()
        }
    }
}
