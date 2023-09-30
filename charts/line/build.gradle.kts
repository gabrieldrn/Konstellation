plugins {
    id("konstellation.android.library")
    id("konstellation.android.publication")
}

version = "${properties["VERSION"]}"

android {
    namespace = "dev.gabrieldrn.konstellation.charts.line"
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
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.timber)
}
