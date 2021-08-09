package com.github.bahamondev.errorlens.services

import com.github.bahamondev.errorlens.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
