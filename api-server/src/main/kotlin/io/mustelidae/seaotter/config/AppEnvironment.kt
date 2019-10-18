package io.mustelidae.seaotter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppEnvironment {
    var uploader: String = "local"
    var localStorage = LocalStorage()
    var awsS3 = AwsS3()

    class LocalStorage {
        var path = Path()
        var url: String = "http://localhost"
        var shardType: String? = "date"
    }

    class AwsS3 {
        var bucket: String = "sea-otter"
        var cloudFront = CloudFront()
        var path = Path()

        class CloudFront {
            var enabled: Boolean = false
            var url: String = "http://localhost"
        }
    }

    class Path {
        var editedPath: String = "/image/edited"
        var unRetouchedPath: String = "/image/unretouched"
    }
}
