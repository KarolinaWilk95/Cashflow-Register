package cashflow.document;

import cashflow.document.currency_converter.CurrencyConverterService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentAPI modelToApi(Document document);

    Document apiToModel(DocumentAPI documentAPI);

}
