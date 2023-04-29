plugins {
    id("konstellation.android.application")
}

android {
    namespace = "dev.gabrieldrn.konstellationdemo"

    defaultConfig {
        applicationId = "dev.gabrieldrn.konstellationdemo"
        versionCode = 1
        versionName = "0.1"
    }
}

dependencies {
    implementation(project(":charts:line"))
    implementation(project(":charts:function"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewModel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.material)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    implementation(libs.timber)
    implementation(libs.koin)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
}
