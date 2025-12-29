package format;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AverageFormatterTest {

    @ParameterizedTest
    @CsvSource({
            "4.0, 4.0",
            "3.000, 3.0",
            "3.01000, 3.01",
            "4.5, 4.5",
            "3.6666667, 3.67",
            "0.0, 0.0"
    })
    void testFormat(double input, String expected) {
        assertEquals(expected, AverageFormatter.format(input));
    }
}
