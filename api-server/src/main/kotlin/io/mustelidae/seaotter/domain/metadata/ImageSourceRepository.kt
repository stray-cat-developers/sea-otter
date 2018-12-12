package io.mustelidae.seaotter.domain.metadata

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
internal interface ImageSourceRepository: MongoRepository<ImageSource, ObjectId>
