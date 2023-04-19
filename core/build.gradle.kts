plugins {
    id("konstellation.android.library")
}

android {
    namespace = "com.gabrieldrn.konstellation"
}

//dokkaHtml.configure {
//    dokkaSourceSets {
//        named("main") {
//            noAndroidSdkLink.set(false)
//        }
//    }
//}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material)
}
