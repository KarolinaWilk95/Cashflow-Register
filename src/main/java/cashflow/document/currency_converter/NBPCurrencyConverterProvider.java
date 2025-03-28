package cashflow.document.currency_converter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NBPCurrencyConverterProvider {


    private final RestTemplate restTemplate;

    public CurrencyConverter getExchangeRate(String code) {

        NBPExchangeRateResponse response = restTemplate.getForObject("https://api.nbp.pl/api/exchangerates/rates/c/" + code + "/today?format=json", NBPExchangeRateResponse.class);
        var list = List.of(response.getRates());


        return new CurrencyConverter(code, list.getFirst().getFirst().getBid());
    }
}
