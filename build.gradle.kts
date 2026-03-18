plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Source: https://mvnrepository.com/artifact/io.github.thexxiv/mealdb
    implementation("io.github.thexxiv:mealdb:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}