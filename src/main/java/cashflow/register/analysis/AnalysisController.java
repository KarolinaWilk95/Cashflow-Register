package cashflow.register.analysis;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AnalysisMapper analysisMapper;

    @PreAuthorize("hasAnyRole('CONTROLLING', 'MANAGEMENT')")
    @GetMapping("api/register")
    public AnalysisAPI analysis() {
        Analysis analysis = analysisService.analysis();
        return analysisMapper.modelToApi(analysis);
    }
}
