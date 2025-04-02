package cashflow.document.currency_converter.provider.nbp;

import lombok.Data;

import java.util.List;

@Data
public class NBPExchangeRateResponse {

    String table;
    String currency;
    String code;

    List<NBPExchangeRate> rates;

}
