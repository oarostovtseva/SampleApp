@file:Suppress("unused")

// All val are used as dependencies

/**
 * Contains dependencies are used in project
 */

object Versions {
    const val build_tools = "28.0.3"
    const val target_sdk = 28
    const val compile_sdk = target_sdk
    const val min_sdk = "21"
    const val versionName = "0.0.1"

    const val kotlin = "1.3.41"
}

object Plugins {
    private const val android_gradle_plugin = "3.3.2"
    private const val versions_gradle_plugin = "0.21.0"

    val android_gradle = "com.android.tools.build:gradle:$android_gradle_plugin"
    val kotlin_gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val versions_gradle = "com.github.ben-manes:gradle-versions-plugin:$versions_gradle_plugin"
}

object Kotlin {
    private const val coroutines_version = "1.2.2"

    val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
}

object AndroidX {
    private const val androidx_version = "1.0.0"
    private const val androidx_test_version = "1.2.0"
    private const val constraint_layout_version = "1.1.3"

    val core = "androidx.core:core-ktx:$androidx_version"
    val app_compat = "androidx.appcompat:appcompat:$androidx_version"
    val recyclerview = "androidx.recyclerview:recyclerview:$androidx_version"
    val constraint_layout = "androidx.constraintlayout:constraintlayout:$constraint_layout_version"
    val design = "com.google.android.material:material:$androidx_version"
    val test_runner = "androidx.test:runner:$androidx_test_version"
    val test_rules = "androidx.test:rules:$androidx_test_version"
}

object Lifecycle {
    private const val lifecycle_version = "2.0.0"
    private const val viewmodel_ktx_version = "2.2.0-alpha01" // not sure about alpha here

    val extensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    val compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$viewmodel_ktx_version"
}

object Koin {
    private const val koin_version = "2.0.1"

    val viewmodel = "org.koin:koin-androidx-viewmodel:$koin_version"
    val core = "org.koin:koin-core:$koin_version"
    val android = "org.koin:koin-android:$koin_version"
    val test = "org.koin:koin-test:$koin_version"
}

object Networking {
    // need to migrate to 2.6.0 to get rid coroutine adapter, see https://github.com/square/retrofit/blob/master/CHANGELOG.md#version-260-2019-06-05
    private const val retrofit_version = "2.5.0"
    private const val logging_interceptor_version = "3.12.1"
    private const val coroutine_adapter_version = "0.9.2"

    val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    val retrofit_converter_moshi = "com.squareup.retrofit2:converter-moshi:$retrofit_version"
    val retrofit_coroutine_adapter =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$coroutine_adapter_version"
    val okhttp_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:$logging_interceptor_version"
}


object Testing {
    private const val mockk_version = "1.9" // 1.9.3 doesn't compile. Need to check it
    private const val junit_version = "5.5.0"
    private const val arch_core_version = "2.0.1"
    private const val espresso_version = "3.2.0"

    val mockk = "io.mockk:mockk:$mockk_version"
    val mockk_android = "io.mockk:mockk-android:$mockk_version"
    val junit_jupiter = "org.junit.jupiter:junit-jupiter:$junit_version"
    val arch_core = "androidx.arch.core:core-testing:$arch_core_version"
    val espresso_contrib = "androidx.test.espresso:espresso-contrib:$espresso_version"
    val espresso_core = "androidx.test.espresso:espresso-core:$espresso_version"
}

object Timber {
    private const val timber_version = "4.7.1"

    val timber = "com.jakewharton.timber:timber:$timber_version"
}
