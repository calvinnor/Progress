@file: JvmName("DaggerInjection")

package com.calvinnor.progress.injection

lateinit var dependencyComponent: DependencyComponent

fun initialise() {
    dependencyComponent = buildDependencyComponent()
}

private fun buildDependencyComponent() = DaggerDependencyComponent
        .builder()
        .dependencyProvider(DependencyProvider)
        .build()
