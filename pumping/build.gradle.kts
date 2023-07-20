import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.4"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("jacoco")
	kotlin("jvm") version "1.7.20"
	kotlin("plugin.spring") version "1.7.20"
}

group = "com.dpm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val asciidoctorExt: Configuration by configurations.creating

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	implementation("io.swagger:swagger-annotations:1.6.8")
	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.0"))
	implementation("io.awspring.cloud:spring-cloud-aws-starter-parameter-store")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

val snippetsDir by extra {
	file("build/generated-snippets")
}

jacoco {
	toolVersion = "0.8.9"
}

tasks {
	asciidoctor {
		dependsOn(test)
		inputs.dir(snippetsDir)
		baseDirFollowsSourceFile()
		configurations("asciidoctorExt")
	}

	register<Copy>("copy") {
		dependsOn(asciidoctor)
		from(file("build/docs/asciidoc"))
		into("src/main/resources/static/docs")
	}

	bootJar {
		dependsOn("copy")
		from (asciidoctor.get().outputDir) {
			into("BOOT-INF/classes/static/docs")
		}
		duplicatesStrategy = DuplicatesStrategy.INCLUDE
	}

	test {
		useJUnitPlatform()
		outputs.dir(snippetsDir)
		finalizedBy(jacocoTestReport)   // report is always generated after tests run
	}

	jacocoTestReport {
		reports {
			html.required.set(true)
			xml.required.set(false)
			csv.required.set(true)   // sonarcube
		}

		finalizedBy("jacocoTestCoverageVerification")
	}

	jacocoTestCoverageVerification {
		violationRules {
			rule {
				isEnabled = true
				element = "CLASS"
				includes = listOf("org.gradle.*")

				limit {
					counter = "BRANCH"
					value = "COVEREDRATIO"
					maximum = "0.7".toBigDecimal()
				}

				limit {
					counter = "LINE"
					value = "COVEREDRATIO"
					maximum = "0.7".toBigDecimal()
				}
			}
		}
	}
}
