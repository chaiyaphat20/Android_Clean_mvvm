// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // หรือถ้าใช้ plugin DSL แบบ new plugins block:
        id("com.google.dagger.hilt.android") version "2.44" apply false
}