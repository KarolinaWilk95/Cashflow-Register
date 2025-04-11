package cashflow.register.analysis;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {

    AnalysisAPI modelToApi(Analysis analysis);

    Analysis apiToModel(AnalysisAPI analysisAPI);
}
