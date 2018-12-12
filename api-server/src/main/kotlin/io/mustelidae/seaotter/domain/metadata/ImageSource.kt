package io.mustelidae.seaotter.domain.metadata

import io.mustelidae.seaotter.constant.ImageFileFormat
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class ImageSource(
        @Indexed
        var tags:List<String> = listOf(),
        var description:String?,
        val paths: List<Path> = listOf()
) {
    @Id
    var id: ObjectId = ObjectId()
        private set

    @CreatedDate
    var created:LocalDateTime? = null
        private set

    data class Path(
            val type:Type,
            val path:String,
            val width:Int,
            val height: Int,
            val format: ImageFileFormat
    ){
        enum class Type {
            THUMBNAIL,
            UN_RETOUCHED,
            EDITED
        }
    }
}
