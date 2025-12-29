import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AverageFormatterTest {

    @Test
    void testFormat() {
        // when & then
        assertEquals("4.0", AverageFormatter.format(4.0));
        assertEquals("3.0", AverageFormatter.format(3.000));
        assertEquals("3.01", AverageFormatter.format(3.01000));
        assertEquals("4.5", AverageFormatter.format(4.5));
        assertEquals("3.67", AverageFormatter.format(3.6666667));
        assertEquals("0.0", AverageFormatter.format(0.0));
    }
}
