package cashflow.document.currency_converter;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Service
@Data
public class CurrencyConverterService {

    private final NBPCurrencyConverterProvider nbpCurrencyConverterProvider;

    public BigDecimal purchaseRate(Currency code, BigDecimal amount) {

        var converterPurchaseRate = nbpCurrencyConverterProvider.getExchangeRate(code.getCurrencyCode());

        return amount.multiply(converterPurchaseRate.getPurchaseRate()).setScale(2, RoundingMode.HALF_EVEN);

    }
}
