plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("me.joypri:joy-data-kotlin:0.0.1")
    implementation("parts.wisdom:wisdom-kotlin:0.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    implementation("org.tensorflow:tensorflow-framework:0.3.0-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "15"
}
