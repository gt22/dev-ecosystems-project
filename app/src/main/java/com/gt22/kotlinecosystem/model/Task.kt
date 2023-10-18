package com.gt22.kotlinecosystem.model

import kotlinx.serialization.Serializable

enum class Language {
    NATURAL,
    KOTLIN,
    JAVA,
    CPP,
    PYTHON,
    HASKELL,
    RUST,
    NIM,
    OTHER;
}

@Serializable
data class Task(val description: String, val language: Language) : java.io.Serializable

data class TaskRef(val id: Int, val task: Task) : java.io.Serializable