plugins {
    id("konstellation.android.library")
}

android {
    namespace = "com.gabrieldrn.konstellation.charts.line"
}

//dokkaHtml.configure {
//    dokkaSourceSets {
//        named("main") {
//            noAndroidSdkLink.set(false)
//        }
//    }
//}

dependencies {
    api(project(":core"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.material)
    implementation(libs.timber)
}
