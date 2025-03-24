package cashflow.document.currency_converter;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class NBPExchangeRate {

    String tableNumber;
    LocalDate effectiveDate;
    BigDecimal bid;

}
