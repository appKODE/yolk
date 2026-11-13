import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id(libs.plugins.kotlinMultiplatform.get().pluginId)
  alias(libs.plugins.vanniktech.mavenPublish)
}

group = providers.gradleProperty("pomGroupId").get()
version = providers.gradleProperty("versionName").get()

kotlin {
  explicitApi()
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }

  jvm {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_1_8)
    }
  }
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":yolk"))
        api(libs.sqlDelightRuntime)
        implementation(libs.bundles.coroutines)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(libs.bundles.koTestCommon)
        implementation(libs.turbine)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.bundles.koTestJvm)
      }
    }
  }
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
      org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
      org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
    )
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}

mavenPublishing {
  coordinates(artifactId = "yolk-sqldelight-extensions")

  publishToMavenCentral()
  signAllPublications()

  pom {
    val pomUrl: String by project
    val pomScmUrl: String by project
    val pomScmConnection: String by project
    val pomScmDevConnection: String by project
    val pomLicenseName: String by project
    val pomLicenseUrl: String by project
    val pomLicenseDist: String by project

    name.set("yolk-sqldelight-extensions")
    description.set("SqlDelight extensions for the yolk loading cache")
    url.set(pomUrl)

    scm {
      url.set(pomScmUrl)
      connection.set(pomScmConnection)
      developerConnection.set(pomScmDevConnection)
    }

    licenses {
      license {
        name.set(pomLicenseName)
        url.set(pomLicenseUrl)
        distribution.set(pomLicenseDist)
      }
    }

    developers {
      developer {
        id.set("dimsuz")
        name.set("Dmitry Suzdalev")
      }
      developer {
        id.set("appKODE")
        name.set("appKODE")
        url.set("https://github.com/appKODE")
      }
    }
  }
}
