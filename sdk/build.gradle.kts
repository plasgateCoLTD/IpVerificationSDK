plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    `signing`
}

android {
    namespace = "com.plasgate.ipverification"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    // Retrofit & Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (Networking & Logging)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // AndroidX Core Library
    implementation("androidx.core:core-ktx:1.9.0")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.0")

    // AndroidX Test Dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }

            groupId = "com.plasgate.ipverification"
            artifactId = "ipverification-sdk"
            version = "1.0.0"

            pom {
                name.set("PlasGate IPVerification SDK")
                description.set("Kotlin SDK for IP Verification via PlasGate")
                url.set("https://github.com/PlasGate/PlasGateIPVerificationSDK")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("kimsea")
                        name.set("Kimsea Sok")
                        email.set("cto@plasgate.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/PlasGate/PlasGateIPVerificationSDK.git")
                    developerConnection.set("scm:git:ssh://github.com/PlasGate/PlasGateIPVerificationSDK.git")
                    url.set("https://github.com/PlasGate/PlasGateIPVerificationSDK")
                }
            }
        }
    }
}


signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_SIGNING_KEY"),
        System.getenv("GPG_SIGNING_PASSPHRASE")
    )
    sign(publishing.publications["release"])
}
