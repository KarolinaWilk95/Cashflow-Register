package cashflow.register.payables;

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
public class PayableController {

    private final PayableService payableService;
    private final PayableMapper payableMapper;
    private final ObjectMapper objectMapper;


    @GetMapping("api/register/payables")
    public List<PayableAPI> showAll() {
        var list = payableService.showAll();
        return list.stream().map(payableMapper::modelToApi).toList();
    }

    @PreAuthorize("hasAnyRole('CONTROLLING', 'DOCUMENT-CIRCULATION')")
    @PatchMapping(value = "api/register/payables/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> partialUpdateDocument(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        var documentFromRepository = payableService.findById(id);

        try {
            var document = applyPatchToDocument(jsonPatch, documentFromRepository);
            payableService.debtEnforcement(document);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private Payable applyPatchToDocument(JsonPatch patch, Payable payable) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(payable, JsonNode.class));
        return objectMapper.treeToValue(patched, Payable.class);
    }

    @GetMapping("api/register/payables/overdue")
    public List<PayableAPI> createReportAboutOverdueReceivables() {

        var result = payableService.createReportAboutOverduePayables();

        return result.stream().map(payableMapper::modelToApi).toList();
    }

    @GetMapping("api/register/payables/overdue/grouped")
    public List<String> createReportAboutOverdueReceivablesGrouped() {

        return payableService.createReportAboutOverduePayablesGrouped();
    }

    @GetMapping("api/register/payables/aging")
    public List<PayableAPI> createReportAboutOverdueReceivablesAging() {

        return payableService.createReportAboutPayablesAging().stream().map(payableMapper::modelToApi).toList();
    }


}
