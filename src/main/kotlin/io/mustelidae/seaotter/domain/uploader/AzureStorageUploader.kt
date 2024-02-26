package io.mustelidae.seaotter.domain.uploader

import com.azure.core.util.BinaryData
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.BlobHttpHeaders
import com.azure.storage.common.StorageSharedKeyCredential
import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Support azure
 * @ref https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/storage/azure-storage-blob
 * Azure Client가 Thread safe한지 확인해서 client를 수정하자
 */
internal class AzureStorageUploader(
    private val azureStorage: AppEnvironment.AzureStorage,
    override val topicCode: String?
) : Uploader {

    override fun upload(image: Image): String {
        val directoryPath = DirectoryPath(azureStorage.path, azureStorage.shardType, topicCode)
        val credential = StorageSharedKeyCredential(azureStorage.accountName, azureStorage.accountKey)

        val blobClient = BlobServiceClientBuilder()
            .endpoint(azureStorage.endpoint)
            .credential(credential)
            .buildClient()
            .getBlobContainerClient(directoryPath.getPath())
            .getBlobClient("${image.name}.${image.getExtension()}")

        val out = ByteArrayOutputStream()
        ImageIO.write(image.bufferedImage, image.getExtension(), out)
        val byteArray = out.toByteArray()

        val headers = BlobHttpHeaders().apply {
            contentType = image.imageFileFormat.mediaType
        }
        blobClient.setHttpHeaders(headers)

        blobClient.upload(BinaryData.fromBytes(byteArray))
        return blobClient.blobUrl
    }

    override fun upload(multipartFile: MultipartFile): String {
        val directoryPath = DirectoryPath(azureStorage.path, azureStorage.shardType, topicCode)
        val credential = StorageSharedKeyCredential(azureStorage.accountName, azureStorage.accountKey)

        val blobClient = BlobServiceClientBuilder()
            .endpoint(azureStorage.endpoint)
            .credential(credential)
            .buildClient()
            .getBlobContainerClient(directoryPath.getPath())
            .getBlobClient(multipartFile.originalFilename)

        blobClient.upload(multipartFile.inputStream, multipartFile.size)
        return blobClient.blobUrl
    }
}
