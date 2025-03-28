package cashflow.register.payables;

import cashflow.document.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface PayableMapper {

    default BigDecimal unpaidAmount(Document document) {

        if (document.getPaymentAmount() == null) {
            document.setPaymentAmount(BigDecimal.ZERO);
        }

        return document.getTotalAmount().subtract(document.getPaymentAmount());
    }

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "documentNumber", source = "document.documentNumber")
    @Mapping(target = "issueDate", source = "document.issueDate")
    @Mapping(target = "dueDate", source = "document.dueDate")
    @Mapping(target = "contractorName", source = "document.contractorName")
    @Mapping(target = "totalAmount", source = "document.totalAmount")
    @Mapping(target = "paymentAmount", source = "document.paymentAmount")
    @Mapping(target = "unpaidAmount", expression = "java(unpaidAmount(payable.getDocument()))")
    @Mapping(target = "currencyCode", source = "document.currencyCode")
    @Mapping(target = "totalAmountInPln", source = "document.totalAmountInPln")
    PayableAPI modelToApi(Payable payable);


    @Mapping(source = "documentId", target = "document.id")
    @Mapping(source = "documentNumber", target = "document.documentNumber")
    @Mapping(source = "issueDate", target = "document.issueDate")
    @Mapping(source = "dueDate", target = "document.dueDate")
    @Mapping(source = "contractorName", target = "document.contractorName")
    @Mapping(source = "totalAmount", target = "document.totalAmount")
    @Mapping(source = "paymentAmount", target = "document.paymentAmount")
    @Mapping(source = "currencyCode", target = "document.currencyCode")
    @Mapping(source = "totalAmountInPln", target = "document.totalAmountInPln")
    Payable apiToModel(PayableAPI payableAPI);


    @Mapping(target = "documentId", source = "id")
    @Mapping(target = "unpaidAmount", expression = "java(unpaidAmount(document))")
    PayableAPI documentToApi(Document document);
}
