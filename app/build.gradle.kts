plugins {
    id("com.android.application")
}

android {
    namespace = "com.xiaosheng.testtempature"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xiaosheng.testtempature"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.github.huangyanbin:SmartTable:2.2.0")
    implementation ("com.blankj:utilcodex:1.30.1")

    implementation("androidx.room:room-runtime:2.2.5")
    annotationProcessor("androidx.room:room-compiler:2.2.5")

    implementation ("com.alibaba.fastjson2:fastjson2:2.0.49")
    implementation("io.github.razerdp:BasePopup:3.2.1")

    implementation ("com.github.HanHuoBin:BaseDialog:1.2.6")

    implementation ("com.github.li-xiaojun:XPopup:2.2.9")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
}