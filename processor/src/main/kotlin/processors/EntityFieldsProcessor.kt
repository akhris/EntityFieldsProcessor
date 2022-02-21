package processors

import annotations.FieldsEntity
import com.squareup.kotlinpoet.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(EntityFieldsProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityFieldsProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(FieldsEntity::class.java.canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        println("processing  in $this")
        val generatedSourcesRoot: String = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()
        if (generatedSourcesRoot.isEmpty()) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Can't find the target directory for generated Kotlin files."
            )
            return false
        }

        roundEnv.getElementsAnnotatedWith(FieldsEntity::class.java).forEach { e ->
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "processing element: ${e.simpleName}"
            )
            //map entity here:
            processEntity(e)
        }

        return true
    }

    private fun processEntity(e: Element) {
        println("processing entity")
        println("name: ${e.simpleName}")
        e.enclosedElements.forEach {
            val elementName = it.simpleName.toString()
            val fieldName = try {
                elementName.substring(0, elementName.indexOf('$'))
            } catch (e: StringIndexOutOfBoundsException) {
                "<empty>"
            }
            println("element: $elementName")
            println("field: $fieldName")
        }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}