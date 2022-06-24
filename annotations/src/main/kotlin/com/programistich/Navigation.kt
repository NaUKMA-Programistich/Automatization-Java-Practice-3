package com.programistich

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class Navigation(
    val destination: String,
    val parameters: Array<String> = []
)
