pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven { setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots") }
        maven{setUrl( "http://maven.aliyun.com/nexus/content/groups/public/")}
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/spring/")}
        maven{setUrl("https://www.jitpack.io")}
    }
}

rootProject.name = "testTempature"
include(":app")
 