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
        //카카오맵
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        //키해시용
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "android-map-location"
include(":app")
