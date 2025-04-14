package cashflow.register.analysis;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {


    default BigDecimal income(Analysis analysis) {
        return analysis.getReceivablesSummary().subtract(analysis.getPayablesSummary());
    }

    @Mapping(target = "income", expression = "java(income(analysis))")
    AnalysisAPI modelToApi(Analysis analysis);

    Analysis apiToModel(AnalysisAPI analysisAPI);
}
