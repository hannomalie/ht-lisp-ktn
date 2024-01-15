import kotlin.test.Test
import kotlin.test.assertContentEquals

class TokenizerTest {

    @Test
    fun `string gets tokenized`() {
        val input = """(toString (+ 1 2))"""

        val tokens = input.tokenize()
        assertContentEquals(listOf("(", "toString", "(", "+", "1", "2", ")", ")"), tokens)
    }
}