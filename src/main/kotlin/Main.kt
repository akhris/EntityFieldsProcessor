import annotations.FieldsEntity
import annotations.IgnoreField
import annotations.SpecifyEntityClass


fun main(args: Array<String>) {
    println("Hello World!")
    val e1: Entity1 = Entity1("Hello", "Entity 1")
    val e2: Entity2 = Entity2("Hello", 1L, e1)
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}


@FieldsEntity(Entity1::class)
data class Entity1(
    val name: String,
    val description: String
)

@FieldsEntity(Entity2::class)
data class Entity2(
    @IgnoreField
    val name: String,
    var count: Long,
    @SpecifyEntityClass(Entity1::class)
    val entity1: Entity1
)