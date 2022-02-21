package processors

import annotations.FieldsEntity
import com.squareup.kotlinpoet.*
import field_mappers.EntityFieldID
import field_mappers.IFieldsMapper
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic
import kotlin.reflect.KClass


@Suppress("LocalVariableName")
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


        roundEnv
            .getElementsAnnotatedWith(FieldsEntity::class.java).forEach { e ->
            val packageName = processingEnv.elementUtils.getPackageOf(e).toString() //package
            val elementName = e.asType()

            val typeUtils = processingEnv.typeUtils
            val typeElement = processingEnv.typeUtils.asElement(e.asType()) as TypeElement

            println(
                """Element info:
                packageName: $packageName
                elementName: $elementName
                qualifiedName: ${typeElement.qualifiedName}"""
            )

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
        println("name: ${e}")
        println("type: ${e.asType()}")

        val typeBuilder =

            TypeSpec
                .classBuilder("${e.simpleName}FieldsMapper")
                .addSuperinterface(IFieldsMapper::class)

        val fields = e
            .enclosedElements
            .filter { it.kind == ElementKind.FIELD }

        val funSpec_getEntityIDs =
            FunSpec
                .builder("getEntityIDs")
                .addParameter(name = "entity", type = Any::class)
                .addModifiers(KModifier.OVERRIDE)

        processGetFieldsIDs(funSpec_getEntityIDs, fields)

        println("getEntityIDs fun:\n${funSpec_getEntityIDs.build()}")

        val type = typeBuilder.build()

        val kotlinFile = FileSpec.builder("com.example.helloworld", "HelloWorld")
            .addType(type)
            .build()
        println("made type: $type")
        kotlinFile.writeTo(System.out)
    }

    private fun processGetFieldsIDs(function: FunSpec.Builder, fields: List<Element>) {

        function
            .addStatement("val fieldIDs = listOf(")

        fields.forEachIndexed { index, field ->
            getFieldIDStatement(function, field, isLast = index == fields.size - 1)
        }

        function
            .addStatement(")")


    }

    private fun processField(typeSpec: TypeSpec.Builder, e: Element) {
        val elementName = e.simpleName.toString()
        val kind = e.kind
        val modifiers = e.modifiers
        println("$elementName - $kind - $modifiers")


    }

    private fun getFieldIDStatement(function: FunSpec.Builder, e: Element, isLast: Boolean) {
        println("element: $e kind: ${e.asType().kind}")
        when (e.asType().kind) {

            TypeKind.LONG -> function.addStatement(
                "%T(tag = tag_${e.simpleName},name = ${e.simpleName})${if (isLast) "," else ""}",
                EntityFieldID.LongID::class
            )
            TypeKind.DECLARED -> {

                println("declared: ${e.asType().asTypeName().toString()}")
            }
            else -> {
                //do nothing
            }
        }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}

inline fun <reified T : Annotation> Element.getAnnotationClassValue(f: T.() -> KClass<*>) = try {
    getAnnotation(T::class.java).f()
    throw Exception("Expected to get a MirroredTypeException")
} catch (e: MirroredTypeException) {
    e.typeMirror
}
