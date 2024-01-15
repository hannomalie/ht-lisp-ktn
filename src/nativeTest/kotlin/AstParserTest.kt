import kotlin.test.Test
import kotlin.test.assertEquals

class AstParserTest {
    @Test
    fun `lists get parsed to Ast`() {
        val input = listOf(
            "toString",
            listOf("+", "1", "2")
        )
        val actual = input.parseToAst()
        val expected = Ast.Node(
            Ast.Leaf(Primitive._Symbol("toString")),
            Ast.Node(
                Ast.Leaf(Primitive._Symbol("+")),
                Ast.Leaf(Primitive._Int(1)),
                Ast.Leaf(Primitive._Int(2)),
            ),
            null
        )
        assertEquals(expected, actual)
    }
}