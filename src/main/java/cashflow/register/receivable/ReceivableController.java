package cashflow.register.receivable;

import cashflow.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReceivableController {

    private final ReceivableService receivableService;
    private final ReceivableMapper receivableMapper;
    private final ObjectMapper objectMapper;



    @GetMapping("api/register/receivables")
    public List<ReceivableAPI> showAll() {
        var list = receivableService.showAll();
        return list.stream().map(receivableMapper::modelToApi).toList();
    }

    @GetMapping("api/register/receivables/{id}")
    public ReceivableAPI findById(@PathVariable Long id) {
        var receivable = receivableService.findById(id);
        return receivableMapper.modelToApi(receivable);
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @PatchMapping(value = "api/register/receivables/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> partialUpdateDocument(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        var documentFromRepository = receivableService.findById(id);
        applyPatchToDocument(jsonPatch, documentFromRepository, id);

        return ResponseEntity.noContent().build();
    }

    private void applyPatchToDocument(JsonPatch patch, Receivable receivable, Long id) {
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(receivable, JsonNode.class));
            Receivable receivableUpdated = objectMapper.treeToValue(patched, Receivable.class);
            receivableService.partialUpdateDocument(receivableUpdated, id);
        } catch (JsonPatchException | JsonProcessingException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ResourceNotFoundException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("api/register/receivables/overdue")
    public List<ReceivableAPI> createReportAboutOverdueReceivables() {

        var result = receivableService.createReportAboutOverdueReceivables();

        return result.stream().map(receivableMapper::modelToApi).toList();
    }

    @GetMapping("api/register/receivables/overdue/grouped")
    public List<String> createReportAboutOverdueReceivablesGrouped() {

        return receivableService.createReportAboutOverdueReceivablesGrouped();
    }

    @GetMapping("api/register/receivables/aging")
    public List<ReceivableAPI> createReportAboutOverdueReceivablesAging() {

        return receivableService.createReportAboutReceivablesAging().stream().map(receivableMapper::modelToApi).toList();
    }


}
