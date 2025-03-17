package cashflow.register.receivable;

import cashflow.document.Document;
import cashflow.register.payables.Payable;
import cashflow.register.payables.PayableAPI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceivableMapper {

    Receivable apiToModel(ReceivableAPI receivableAPI);

    ReceivableAPI modelToApi(Receivable Receivable);

    Receivable documentToReceivable(Document document);
}
