package cashflow.document.currency_converter.provider.nbp;

import cashflow.document.currency_converter.CurrencyRateDetails;
import cashflow.document.currency_converter.provider.CurrencyRateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NBPCurrencyRateProvider implements CurrencyRateProvider {


    private final RestTemplate restTemplate;

    public CurrencyRateDetails getExchangeRate(String code) {

        NBPExchangeRateResponse response = restTemplate.getForObject("https://api.nbp.pl/api/exchangerates/rates/c/" + code + "/today?format=json", NBPExchangeRateResponse.class);
        var list = List.of(response.getRates());


        return new CurrencyRateDetails(code, list.getFirst().getFirst().getBid());
    }
}
