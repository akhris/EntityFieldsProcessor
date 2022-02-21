import annotations.FieldsEntity

fun main(args: Array<String>) {
    println("Hello World!")
    val e1: Entity1 = Entity1("Hello", "Entity 1")
    val e2: Entity2 = Entity2("Hello", 1L, e1)
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}


@FieldsEntity
data class Entity1(
    val name: String,
    val description: String
)

@FieldsEntity
data class Entity2(
    val name: String,
    val count: Long,
    val entity1: Entity1
)