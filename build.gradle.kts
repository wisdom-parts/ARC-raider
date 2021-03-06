plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("me.joypri:joy-data-kotlin:0.0.1")
    implementation("parts.wisdom:wisdom-kotlin:0.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}
//
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions.jvmTarget = "15"
//}
