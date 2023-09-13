import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example.docs"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.8.0"
//    `java-gradle-plugin`
    `maven-publish`
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("java-test-fixtures")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    asciidoctor {
        setSourceDir(file("docs"))
        setOutputDir(file("build/docs"))
    }

    bootJar {
        val asciidoctor = asciidoctor.get()

        setDependsOn(listOf(asciidoctor))
        from("${asciidoctor.outputDir}/html5") {
            into("static/docs")
        }
    }
}

//gradlePlugin {
//    plugins {
//        create("hello") {
//            id = "growing-docs"
//            implementationClass = "com.example.docs.GreetingPlugin"
//        }
//    }
//}

publishing {
    repositories {
        mavenLocal() // TODO - maven 저장소 구축 후 연동
    }
}

val asciidoctorExtensions: Configuration by configurations.creating
dependencies {
    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")

    testFixturesImplementation("com.google.guava:guava:31.0.1-jre")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testFixturesApi("org.springframework.restdocs:spring-restdocs-restassured")
}
