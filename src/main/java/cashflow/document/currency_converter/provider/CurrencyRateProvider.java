package cashflow.document.currency_converter.provider;

import cashflow.document.currency_converter.CurrencyRateDetails;

public interface CurrencyRateProvider {

    CurrencyRateDetails getExchangeRate(String code);
}
