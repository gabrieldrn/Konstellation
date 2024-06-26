plugins {
    id("konstellation.android.library")
}

android {
    namespace = "dev.gabrieldrn.konstellation.charts.function"
}

// ! Excluded as implementation is not sufficient enough.
//dokkaHtml.configure {
//    dokkaSourceSets {
//        named("main") {
//            noAndroidSdkLink.set(false)
//        }
//    }
//}

dependencies {
    api(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.material)
}
