import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.calendardemo23124"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.calendardemo23124"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //dependencies {
    implementation("com.google.android.gms:play-services-auth:20.7.0")
   // implementation("com.google.api-client:google-api-client-android:2.2.0")

    implementation("com.google.oauth-client:google-oauth-client-jetty:1.31.5")
    // https://mvnrepository.com/artifact/com.google.apis/google-api-services-calendar
    implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")


    //for google auth and calendar integrations 201 24 medium
  //  implementation("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    //to avoid conflicts in libraries
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")


   // implementation ("com.google.api-client:google-api-client-android:2.2.0")


       // implementation ("com.google.api-client:google-api-client-android:2.2.0")
        implementation ("com.google.api-client:google-api-client-gson:1.31.5")
        // ...


//    implementation(("com.google.api-client:google-api-client-android:1.23.0")) {
//        exclude("org.apache.httpcomponents")

        //firebase 555

    implementation("com.google.api-client:google-api-client-android:1.23.0") {
        exclude ("org.apache.http components")
    }


    }
