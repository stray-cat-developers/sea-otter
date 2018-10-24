package io.mustelidae.seaotter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "sea-otter")
class OtterEnvironment {
    var uploader: String = "local"
    var localStorage = LocalStorage()
    var awsS3 = AwsS3()

    class LocalStorage {
        var defaultPath: String = "/var/tmp"
        var url: String = "http://localhost"
    }

    class AwsS3 {
        var bucket: String = "sea-otter"
        var defaultPath: String = "multimedia/"
        var cloudFront = CloudFront()

        class CloudFront {
            var enabled: Boolean = false
            var url: String = "http://localhost"
        }
    }
}
