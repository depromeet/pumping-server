import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.4"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
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
	//	implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.0"))
	//	implementation("io.awspring.cloud:spring-cloud-aws-starter-parameter-store")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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

tasks.withType<Test> {
	useJUnitPlatform()
	outputs.dir(snippetsDir)
}

tasks {
	asciidoctor {
		configurations("asciidoctorExt")
		inputs.dir(snippetsDir)
		dependsOn(test)
	}

	bootJar {
		dependsOn(asciidoctor)
		from ("${asciidoctor.get().outputDir}/html5") {
			into("/BOOT-INF/classes/static/docs")
		}
	}
}
