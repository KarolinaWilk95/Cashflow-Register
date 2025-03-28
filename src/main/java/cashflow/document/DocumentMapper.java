package cashflow.document;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {


    DocumentAPI modelToApi(Document document);

    Document apiToModel(DocumentAPI documentAPI);

}
