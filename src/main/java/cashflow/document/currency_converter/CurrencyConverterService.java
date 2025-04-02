package cashflow.document.currency_converter;

import cashflow.document.currency_converter.provider.nbp.NBPCurrencyRateProvider;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Service
@Data
public class CurrencyConverterService {

    private final NBPCurrencyRateProvider nbpCurrencyRateProvider;

    public BigDecimal purchaseRate(Currency code, BigDecimal amount) {

        var converterPurchaseRate = nbpCurrencyRateProvider.getExchangeRate(code.getCurrencyCode());

        return amount.multiply(converterPurchaseRate.purchaseRate()).setScale(2, RoundingMode.HALF_EVEN);

    }
}
