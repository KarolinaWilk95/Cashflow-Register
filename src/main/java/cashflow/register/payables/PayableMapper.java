package cashflow.register.payables;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayableMapper {

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "documentNumber", source = "document.documentNumber")
    @Mapping(target = "issueDate", source = "document.issueDate")
    @Mapping(target = "dueDate", source = "document.dueDate")
    @Mapping(target = "contractorName", source = "document.contractorName")
    @Mapping(target = "totalAmount", source = "document.totalAmount")
    @Mapping(target = "currencyCode", source = "document.currencyCode")
    @Mapping(target = "totalAmountInPln", source = "document.totalAmountInPln")
    PayableAPI modelToApi(Payable payable);

    Payable apiToModel(PayableAPI payableAPI);


}
