pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SplitTrip"
include(":app")
include(":core:common")
include(":core:ui")
include(":core:navigation")
include(":feature:auth")
include(":feature:trips")
include(":feature:settle")
include(":feature:activity")
include(":feature:profile")
include(":core:di")
include(":data:auth")
include(":data:trips")
include(":data:settle")
include(":data:activity")
include(":data:profile")
include(":domain:auth")
include(":domain:trips")
include(":domain:settle")
include(":domain:activity")
include(":domain:profile")
