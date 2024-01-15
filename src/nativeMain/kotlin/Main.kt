sealed interface Ast {
    data class Node(var operation: Leaf, var a: Ast, var b: Ast?): Ast
    data class Leaf(var value: Primitive): Ast
}

sealed interface Primitive {
    data class _String(val value: String): Primitive
    data class _Int(val value: Int): Primitive
    data class _Float(val value: Float): Primitive
    data class _Symbol(val value: String): Primitive
}

fun main(args: Array<String>) {
    val input = if(args.isNotEmpty()) args.first() else "(println (+ (- 3 2) 5))"
    println("Input: $input")

    val tokens = input.tokenize()

    val lists = tokens.parseToLists()
    println("Parsed to lists: $lists")

    val ast = lists.parseToAst()
    println("AST: $ast")

    ast.interpret()
}

fun String.tokenize() = replace("(", "( ").replace(")", " )").split(" ")

fun List<String>.parseToLists(): MutableList<Any> {
    var index = 0
    val stack = mutableListOf<MutableList<Any>>()

    while(index < size) {
        when(val token = this[index]) {
            "(" -> {
                val last = stack.lastOrNull()
                val new = mutableListOf<Any>()
                stack.add(new)
                last?.add(new)
            }
            ")" -> if(stack.size > 1) stack.removeLast()
            else -> {
                stack.last().add(token)
            }
        }
        index++
    }
    return stack.first()
}

fun Any.parseToAst(): Ast {
    return when (this) {
        is String -> {
            val typed = when {
                startsWith(""""""") && endsWith(""""""") -> Primitive._String(this)
                toIntOrNull() != null -> Primitive._Int(toInt())
                toFloatOrNull() != null -> Primitive._Float(toFloat())
                else -> Primitive._Symbol(this)
            }
            Ast.Leaf(typed)
        }

        is List<*> -> {
            val elements = map {
                it!!.parseToAst()
            }
            Ast.Node(elements.first() as Ast.Leaf, elements[1], if(elements.size > 2) elements[2] else null)
        }

        else -> {
            throw IllegalStateException("Unknown data type in value: $this")
        }
    }
}

fun Ast.interpret(): Any = when(this) {
    is Ast.Leaf -> value
    is Ast.Node -> when(val operation = operation.value) {
        Primitive._Symbol("-") -> {
            val resultA = a.interpret() as Primitive._Int
            val resultB = b!!.interpret() as Primitive._Int
            resultA.value - resultB.value
        }
        Primitive._Symbol("+") -> {
            val resultA = a.interpret() as Primitive._Int
            val resultB = b!!.interpret() as Primitive._Int
            resultA.value + resultB.value
        }
        Primitive._Symbol("println") -> {
            val resultA = a.interpret()
            println(resultA)
        }
        else -> throw IllegalArgumentException("Unknown operation: $operation")
    }
}