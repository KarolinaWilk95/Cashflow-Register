package cashflow.document.currency_converter.provider.nbp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class NBPExchangeRate {

    String tableNumber;
    LocalDate effectiveDate;
    BigDecimal bid;

}
