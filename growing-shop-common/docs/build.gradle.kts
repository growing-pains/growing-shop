import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example.docs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    kotlin("jvm") version "1.7.22"
    `java-gradle-plugin`
    `maven-publish`
    id("org.asciidoctor.convert") version "2.4.0"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    asciidoctor {
        sourceDir = file("docs")
        outputDir = file("build/docs")
    }

    bootJar {
        val asciidoctor = asciidoctor.get()

        setDependsOn(listOf(asciidoctor))
        from("${asciidoctor.outputDir}/html5") {
            into("static/docs")
        }
    }
}

gradlePlugin {
    plugins {
        create("hello") {
            id = "growing-docs"
            implementationClass = "com.example.docs.GreetingPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

dependencies {
    asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
}
