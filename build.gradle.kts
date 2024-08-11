plugins {
  java
  id("org.springframework.boot") version "3.2.4"
  id("io.spring.dependency-management") version "1.1.4"
}

group = "org.ovida"
version = "0.0.1"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

configurations {
  configureEach {
    exclude(module = "spring-boot-starter-logging")
  }
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
  }
  compileTestJava {
    options.encoding = "UTF-8"
  }
  jar {
    enabled = false
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-log4j2")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.flywaydb:flyway-core")
  implementation("io.jsonwebtoken:jjwt-api:0.12.5")

  compileOnly("org.projectlombok:lombok")

  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("com.lmax:disruptor:3.4.4")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "junit-jupiter")
    exclude(module = "junit-jupiter-api")
    exclude(module = "mockito-junit-jupiter")
  }
  testImplementation("junit:junit:4.13.2")
  testImplementation("com.googlecode.junit-toolbox:junit-toolbox:2.4")
  testImplementation("org.testcontainers:postgresql:1.20.1")
  testImplementation("pl.pragmatists:JUnitParams:1.1.1")
}

tasks.withType<Test> {
  useJUnit()
  exclude("**/*IT.class")

  testLogging {
    events("passed", "skipped", "failed")
  }
}
