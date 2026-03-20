plugins {
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javafxVersion = "21"

dependencies {
    implementation("org.openjfx:javafx-base:$javafxVersion:win")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:win")
    implementation("org.openjfx:javafx-controls:$javafxVersion:win")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:win")

    implementation("io.github.thexxiv:mealdb:1.0.0")

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
