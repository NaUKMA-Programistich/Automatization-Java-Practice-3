package com.programistich

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Component(
    val name: String,
    val type: Int = 0
)
