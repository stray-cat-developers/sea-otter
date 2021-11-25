package io.mustelidae.seaotter.domain.uploader

import com.amazonaws.annotation.NotThreadSafe
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

/**
 * Upload images to google s3.
 * If the use of the cloud front is enabled, make url the cloud front url.
 */
@NotThreadSafe
internal class AwsS3Uploader(
    private val awsS3: AppEnvironment.AwsS3,
    override val topicCode: String?
) : Uploader {
    private val s3Client = AmazonS3ClientBuilder.standard()
        .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(awsS3.accessKey, awsS3.secretKey)))
        .build()

    override fun upload(image: Image): String {
        val directoryPath = DirectoryPath(awsS3.path, awsS3.shardType, topicCode).apply {
            append(image.isOriginal)
        }

        val out = ByteArrayOutputStream()
        ImageIO.write(image.bufferedImage, image.getExtension(), out)
        val byteArray = out.toByteArray()

        val metaData = ObjectMetadata().apply {
            contentLength = byteArray.size.toLong()
            contentType = "image/${image.getExtension()}"
        }

        val putObjectRequest = PutObjectRequest(awsS3.bucket, directoryPath.getPath(), ByteArrayInputStream(byteArray), metaData).apply {
            withCannedAcl(CannedAccessControlList.PublicRead)
        }

        s3Client.putObject(putObjectRequest)
        return directoryPath.getPath()
    }

    companion object {
        fun makeUrl(awsS3: AppEnvironment.AwsS3, uploadedPath: String): URL {
            val url = awsS3.cloudFront.url + uploadedPath.removePrefix(awsS3.path)
            return URL(url)
        }
    }
}
