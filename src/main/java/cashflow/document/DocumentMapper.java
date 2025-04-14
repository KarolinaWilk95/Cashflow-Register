package cashflow.document;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentAPI modelToApi(Document document);

    Document apiToModel(DocumentAPI documentAPI);

    List<DocumentAPI> listToListAPI(List<Document> document);

}
