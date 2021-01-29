plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("me.joypri:joy-data-kotlin:0.0.1")
    implementation("parts.wisdom.djlkotlin:api:0.0.1")
    implementation("parts.wisdom.djlkotlin:basicdataset:0.0.1")
    implementation("parts.wisdom.djlkotlin:model-zoo:0.0.1")
    implementation("org.slf4j:slf4j-simple:1.7.30")
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
