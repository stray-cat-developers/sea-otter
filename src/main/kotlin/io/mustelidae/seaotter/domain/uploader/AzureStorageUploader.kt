package io.mustelidae.seaotter.domain.uploader

import com.azure.core.util.BinaryData
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.common.StorageSharedKeyCredential
import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
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
        val directoryPath = DirectoryPath(azureStorage.path, azureStorage.shardType, topicCode).apply {
            append(image.isOriginal)
        }

        val credential = StorageSharedKeyCredential(azureStorage.accountName, azureStorage.accountKey)

        val blobClient = BlobServiceClientBuilder()
            .endpoint(azureStorage.endpoint)
            .credential(credential)
            .buildClient()
            .getBlobContainerClient(directoryPath.getPath()).apply {
                create()
            }.getBlobClient(image.name)

        val out = ByteArrayOutputStream()
        ImageIO.write(image.bufferedImage, image.getExtension(), out)
        val byteArray = out.toByteArray()

        blobClient.upload(BinaryData.fromBytes(byteArray))

        return blobClient.blobUrl
    }
}
