package com.gurunars.storybook_registry

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

fun ProcessingEnvironment.buildAnkoFiles(
    roundEnv: RoundEnvironment
) {

    val kaptKotlinGeneratedDir = options[StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "Can't find the target directory for generated Kotlin files.")
        return
    }

    val mapping = roundEnv
        .getElementsAnnotatedWith(StorybookComponent::class.java)
        .filter { it.kind == ElementKind.CLASS }
        .map { it as TypeElement }

    processElement(kaptKotlinGeneratedDir, mapping)
}

fun ProcessingEnvironment.note(msg: String) = messager.printMessage(Diagnostic.Kind.NOTE, msg)
fun ProcessingEnvironment.error(msg: String) = messager.printMessage(Diagnostic.Kind.ERROR, msg)
fun ProcessingEnvironment.warning(msg: String) = messager.printMessage(Diagnostic.Kind.WARNING, msg)

fun ProcessingEnvironment.processElement(
    kaptKotlinGeneratedDir: String,
    elements: List<TypeElement>
) {
    /*
    note("Processing ${element.simpleName}")
    val componentName = element.simpleName.toString()
    val packageName = elementUtils.getPackageOf(element).qualifiedName.toString()
    */

    // TODO: make sure that the component is a no arg extension function of Context
    // TODO: make sure that the component returns a View

    val tpl = """
    package com.gurunars.storybook

    class ActivityStorybook(): AbstractActivityStorybook() {
        override val views: Map<String, RenderDemo> = mapOf(

        )
    }
    """

    try {
        File(kaptKotlinGeneratedDir, "ActivityStorybook.kt").apply {
            parentFile.mkdirs()
            writeText(tpl)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}