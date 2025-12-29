import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.math.RoundingMode.UNNECESSARY;

public final class AverageFormatter {

    private AverageFormatter() {
    }

    public static String format(double value) {
        var bigDecimalValue = valueOf(value).setScale(2, HALF_UP).stripTrailingZeros();
        return bigDecimalValue.scale() <= 0
                ? bigDecimalValue.setScale(1, UNNECESSARY).toPlainString()
                : bigDecimalValue.toPlainString();
    }
}