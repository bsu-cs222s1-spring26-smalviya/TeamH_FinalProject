plugins {
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javafxVersion = "21.0.2"

dependencies {
    // JavaFX
    implementation("org.openjfx:javafx-base:$javafxVersion:win")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:win")
    implementation("org.openjfx:javafx-controls:$javafxVersion:win")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:win")

    // JSON parsing (these 3 work together)
    implementation("com.jayway.jsonpath:json-path:2.9.0")
    implementation("net.minidev:json-smart:2.4.10")   // REQUIRED by JsonPath
    implementation("com.github.openjson:openjson:1.0.12") // Your JSON object parser

    // MealDB wrapper (optional)
    implementation("io.github.thexxiv:mealdb:1.0.0")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("UI.RecipeAppSkeleton")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    val modulePath = configurations.runtimeClasspath.get().asPath
    jvmArgs = listOf(
        "--module-path", modulePath,
        "--add-modules", "javafx.controls,javafx.fxml"
    )
}
