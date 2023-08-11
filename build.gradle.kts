
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.14"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jmailen.kotlinter") version "3.6.0"
    id("com.avast.gradle.docker-compose") version "0.14.9"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("plugin.allopen") version "1.8.22"
    kotlin("plugin.noarg") version "1.8.22"

}

group = "io.mustelidae.seaotter"
version = "0.2.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
}

ext["log4j2.version"] = "2.17.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("com.amazonaws:aws-java-sdk-s3:1.12.523") {
        constraints {
            implementation("com.fasterxml.woodstox", "woodstox-core", "6.5.1") {
                because("CVE-2022-40151, CVE-2022-40152, CVE-2022-40156  7.5 Out-of-bounds Write vulnerability with medium severity found")
            }
            implementation("org.yaml","snakeyaml","2.1") {
                because("Uncontrolled Resource Consumption vulnerability pending CVSS allocation")
            }
        }
    }
    implementation("com.azure:azure-storage-blob:12.23.0") {
        constraints {
            implementation("io.netty", "netty-codec", "4.1.96.Final") {
                because("CVE-2022-41915 6.5 Improper Neutralization of CRLF Sequences in HTTP Headers ('HTTP Response Splitting') vulnerability pending CVSS allocation")
            }
        }
    }

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    //Image Processor
    implementation("org.imgscalr:imgscalr-lib:4.2") // https://github.com/rkalla/imgscalr
    implementation("com.mortennobel:java-image-scaling:0.8.6") // https://github.com/mortennobel/java-image-scaling
    implementation("net.coobird:thumbnailator:0.4.20") // https://github.com/coobird/thumbnailator

    implementation("com.twelvemonkeys.imageio:imageio-jpeg:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-tiff:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-psd:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-bmp:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-pdf:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-hdr:3.9.4")
    implementation("com.twelvemonkeys.servlet:servlet:3.9.4")
    implementation("com.twelvemonkeys.imageio:imageio-webp:3.9.4")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.5")

    implementation("com.google.guava:guava:32.1.2-jre")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-undertow"){
        exclude("io.undertow","undertow-websockets-jsr")
    }
    implementation("org.springframework.boot:spring-boot-starter-web"){
        exclude(module = "spring-boot-starter-tomcat")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("javax.interceptor:javax.interceptor-api:1.2.2")

    implementation("org.mongodb:bson:4.4.0")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.github.kittinunf.fuel:fuel:2.3.1")

}

tasks.register("version") {
    println(version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    failFast = true

    testLogging {
        events.plus(TestLogEvent.FAILED)
        events.plus(TestLogEvent.STANDARD_ERROR)

        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true

        info.events = debug.events
        info.exceptionFormat = debug.exceptionFormat
    }

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if(suite.parent == null){
                val output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                val startItem = "|"
                val endItem = "  |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("\n${"-".repeat(repeatLength)}\n|  $output  |\n${"-".repeat(repeatLength)}")
            }
        }
    })

    minHeapSize = "1024m"
    maxHeapSize = "4096m"
}
