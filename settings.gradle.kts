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
<<<<<<< HEAD
    }
}

rootProject.name = "android-map-location"
=======
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")}
        maven { url = uri("<https://devrepo.kakao.com/nexus/repository/kakaomap-releases/>") }
    }
}

rootProject.name = "android-map-search"
>>>>>>> search/step2
include(":app")
