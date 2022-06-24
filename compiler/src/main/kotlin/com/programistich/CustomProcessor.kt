package com.programistich

import com.squareup.kotlinpoet.* // ktlint-disable no-wildcard-imports
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class CustomProcessor : AbstractProcessor() {

    private lateinit var filer: Filer

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        this.filer = processingEnv.filer
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val annotations: MutableSet<String> = LinkedHashSet()
        annotations.add(Navigation::class.java.canonicalName)
        annotations.add(Component::class.java.canonicalName)
        return annotations
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        processNavigation(roundEnv.getElementsAnnotatedWith(Navigation::class.java))
        processComponent(roundEnv.getElementsAnnotatedWith(Component::class.java))
        return true
    }

    private fun processComponent(elementsAnnotated: Set<Element>) {
        if (elementsAnnotated.isEmpty()) {
            return
        }

        val classBuilder = TypeSpec.classBuilder("DI")

        val fileBuilder = FileSpec
            .builder("", "DI")

        elementsAnnotated.forEach { element ->
            val name = element.simpleName.toString()
            val packet = processingEnv.elementUtils.getPackageOf(element)
            val method = FunSpec.builder(name.lowercase())
                .addCode(
                    """|return $name()""".trimMargin()
                )
                .build()
            fileBuilder.addImport(packet.qualifiedName.toString(), element.simpleName.toString())
            classBuilder.addFunction(method)
        }

        fileBuilder.addType(classBuilder.build()).build().writeTo(filer)
    }

    private fun processNavigation(elementsAnnotated: Set<Element>) {
        if (elementsAnnotated.isEmpty()) {
            return
        }

        val methodBuilder = FunSpec.builder("route")
            .addParameter("destination", String::class)
            .beginControlFlow("when (destination)")

        elementsAnnotated.forEach { element ->
            val method = "${element.simpleName}()"
            val navigation = element.getAnnotation(Navigation::class.java)
            methodBuilder.addStatement("\"${navigation.destination}\" -> $method")
        }
        methodBuilder.addStatement("else -> {}").endControlFlow()

        val classBuilder = TypeSpec
            .classBuilder("NavigationRouter")
            .addFunction(methodBuilder.build())

        val fileBuilder = FileSpec
            .builder("", "NavigationRouter")
            .addType(classBuilder.build())

        elementsAnnotated.forEach { element ->
            val pkg = processingEnv.elementUtils.getPackageOf(element)
            fileBuilder.addImport(pkg.qualifiedName.toString(), element.simpleName.toString())
        }

        fileBuilder.build().writeTo(filer)
    }
}
