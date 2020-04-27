
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        maven("https://palantir.bintray.com/releases/")
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.70"))
        classpath("com.palantir.configurationresolver:gradle-configuration-resolver-plugin:0.4.0")
    }
}

plugins {
    java
    idea
    kotlin("jvm") version "1.3.70"
    kotlin("kapt") version "1.3.70"
    kotlin("plugin.spring") version "1.3.70"
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jmailen.kotlinter") version "1.26.0"
}

group = "io.mustelidae.seaotter"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.register("version") {
    println(version)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failFast = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://palantir.bintray.com/releases/")
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    compile("com.amazonaws:aws-java-sdk-s3:1.11.462")

    // https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload
    compile("commons-fileupload:commons-fileupload:1.4")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    compile("com.twelvemonkeys.imageio:imageio-jpeg:3.4.1")
    compile("com.twelvemonkeys.imageio:imageio-tiff:3.4.1")
    compile("com.twelvemonkeys.imageio:imageio-psd:3.4.1")
    compile("com.twelvemonkeys.imageio:imageio-bmp:3.4.1")
    compile("com.twelvemonkeys.imageio:imageio-pdf:3.4.1")
    compile("com.twelvemonkeys.imageio:imageio-hdr:3.4.1")
    compile("com.twelvemonkeys.servlet:servlet:3.4.1")

    compile("com.github.kittinunf.fuel:fuel:2.2.1")

    compile("com.google.guava:guava:28.1-jre")
    testCompile("com.google.truth:truth:1.0")

    compile("io.springfox:springfox-swagger2:2.9.2")
    compile("io.springfox:springfox-swagger-ui:2.9.2")
    compile("io.springfox:springfox-bean-validators:2.9.2")

    compile("org.springframework.boot:spring-boot-starter")
    testCompile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-web"){
        exclude(module = "spring-boot-starter-tomcat")
    }
    compile("org.springframework.boot:spring-boot-starter-validation")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-configuration-processor")
    compile("org.springframework.boot:spring-boot-starter-undertow"){
        exclude("io.undertow","undertow-websockets-jsr")
    }

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    testCompile("org.springframework.boot:spring-boot-starter-test")

    compile("javax.interceptor:javax.interceptor-api:1.2.2")

    compile("org.mongodb:bson:3.10.2")

    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testCompile("org.junit.platform:junit-platform-launcher:1.3.1")
    testCompile("org.springframework.hateoas:spring-hateoas:0.25.0.RELEASE")
    testImplementation("io.mockk:mockk:1.9")

    //Image Processor
    compile("org.imgscalr:imgscalr-lib:4.2") // https://github.com/rkalla/imgscalr
    compile("com.mortennobel:java-image-scaling:0.8.6") // https://github.com/mortennobel/java-image-scaling
    compile("net.coobird:thumbnailator:0.4.8") // https://github.com/coobird/thumbnailator
}

tasks.withType<Test> {
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
                val startItem = "|  "
                val endItem = "  |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("\n${"-".repeat(repeatLength)}\n|  $output  |\n${"-".repeat(repeatLength)}")
            }
        }
    })
}
