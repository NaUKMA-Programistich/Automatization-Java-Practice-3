package com.programistich

class PositiveImpl {
    fun addTwo(@Positive a: Int, @Positive b: Int): Int {
        val method = javaClass.getMethod("addTwo", Int::class.java, Int::class.java)
        method.parameters.filter { it.isAnnotationPresent(Positive::class.java) }.forEach {
            if (it.name == "arg0" && a < 0) throw RuntimeException("")
            if (it.name == "arg1" && b < 0) throw RuntimeException("")
        }
        return a + b
    }
}

fun main() {
    // println(PositiveImpl().addTwo(2, -1))
    // println(PositiveImpl().addTwo(-2, 1))
    // println(PositiveImpl().addTwo(-2, -1))
    println(PositiveImpl().addTwo(2, 2))
}
