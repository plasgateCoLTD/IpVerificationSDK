plugins {
    id("com.android.library") version "8.2.2" apply false
    kotlin("android") version "1.9.10" apply false
}


allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
