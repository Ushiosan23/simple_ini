package project

enum class TypeDependency(val entry: String) {
    IMPLEMENTATION("implementation"),
    COMPILE_ONLY("compileOnly"),
    TEST_IMPLEMENTATION("testImplementation"),
    TEST_COMPILE_ONLY("testCompileOnly")
}