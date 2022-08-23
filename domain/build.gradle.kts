@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.buildkonfig.get().pluginId)
    id(libs.plugins.kotlinter.get().pluginId)
}

kotlin {
    android {
        compilations {
            all {
                kotlinOptions.jvmTarget = Config.androidJvmTarget.toString()
            }
        }
    }
    jvm("desktop") {
        compilations {
            all {
                kotlinOptions.jvmTarget = Config.desktopJvmTarget.toString()
            }
        }
    }
    iosX64()
    iosArm64()
    //iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api(kotlin("stdlib-common"))
                api(libs.coroutines.core)
                api(libs.serialization.json.core)
                api(libs.serialization.json.okio)
                api(libs.kotlinInject.runtime)
                api(libs.ktor.core)
                api(libs.ktor.contentNegotiation)
                api(libs.ktor.serialization.json)
                api(libs.ktor.auth)
                api(libs.ktor.logging)
                api(libs.ktor.websockets)
                api(libs.okio)
                api(libs.dateTime)
                api(projects.core)
                api(projects.i18n)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(libs.ktor.okHttp)
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api(kotlin("stdlib-jdk8"))
            }
        }
        val desktopTest by getting

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api(kotlin("stdlib-jdk8"))
            }
        }
        val androidTest by getting

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(libs.ktor.darwin)
            }
        }
        val iosTest by creating {
            dependsOn(commonTest)
        }

        listOf("iosX64Main", "iosArm64Main"/*, "iosSimulatorArm64Main"*/).forEach {
            getByName(it).dependsOn(iosMain)
        }
        listOf("iosX64Test", "iosArm64Test"/*, "iosSimulatorArm64Test"*/).forEach {
            getByName(it).dependsOn(iosTest)
        }
    }
}

dependencies {
    add("kspDesktop", libs.kotlinInject.compiler)
    add("kspAndroid", libs.kotlinInject.compiler)
}

buildkonfig {
    packageName = "ca.gosyer.jui.domain.build"
}
