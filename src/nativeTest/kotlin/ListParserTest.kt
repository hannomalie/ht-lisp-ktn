import kotlin.test.Test
import kotlin.test.assertContentEquals

class ListParserTest {

    @Test
    fun `tokens get parsed to list`() {
        val tokens = listOf("(", "toString", "(", "+", "1", "2", ")", ")")

        assertContentEquals(
            listOf(
                "toString",
                listOf("+", "1", "2")
            ),
            tokens.parseToLists()
        )
    }
}