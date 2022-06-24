package com.programistich

class PositiveImpl {
    fun addTwo(@Positive a: Int, @Positive b: Int): Int {
        val method = javaClass.getMethod("addTwo", Int::class.java, Int::class.java)
        method.parameters.filter { it.isAnnotationPresent(Positive::class.java) }.forEach {
            val value = it.getAnnotation(Positive::class.java).size
            if (it.name == "arg0" && a < value) throw RuntimeException("")
            if (it.name == "arg1" && b < value) throw RuntimeException("")
        }
        return a + b
    }
}
