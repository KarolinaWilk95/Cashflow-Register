package cashflow.document.currency_converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CurrencyConverter {
    String code;
    BigDecimal purchaseRate;
}
