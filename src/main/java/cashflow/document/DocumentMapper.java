package cashflow.document;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {


    DocumentAPI modelToApi(Document document);

    Document apiToModel(DocumentAPI documentAPI);

}
