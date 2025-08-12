plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
}

group = "com.maninv"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/*Application.*",
        "**/config/**",
        "**/dto/**",
        "**/exception/**",
        "**/entity/**",
        "**/*MapperImpl.*"
    )
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(fileFilter)
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            excludes = listOf(
                "**/*Application.*",
                "**/config/**",
                "**/dto/**",
                "**/exception/**",
                "**/entity/**",
                "**/*MapperImpl.*"
            )
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
    dependsOn(tasks.jacocoTestCoverageVerification)
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    implementation ("org.mapstruct:mapstruct:1.6.0")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.6.0")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
